package com.checkping.service.project;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import com.checkping.domain.project.ProgressStep;
import com.checkping.domain.project.Project;
import com.checkping.domain.project.projection.ProjectCountByManagementStep;
import com.checkping.domain.project.projection.ProjectListInfoByManagementStep;
import com.checkping.dto.project.ProjectResponse;
import com.checkping.infra.dto.ProjectListDetailsDto;
import com.checkping.infra.dto.ProjectUpdateDetailsDto;
import com.checkping.infra.repository.member.MemberRepository;
import com.checkping.infra.repository.member.OrganizationRepository;
import com.checkping.infra.repository.project.ProgressStepRepository;
import com.checkping.infra.dto.ProjectDetailsDto;
import com.checkping.infra.repository.project.projection.ProjectInfoProjection;
import com.checkping.infra.repository.project.ProjectRepository;
import com.checkping.dto.project.ProjectRequest;

import com.checkping.service.member.util.CurrentMemberUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.stream.Collectors;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;
    private final MemberRepository memberRepository;
    private final ProgressStepRepository progressStepRepository;
    private final CurrentMemberUtil currentMemberUtil;

    @Override
    @Transactional
    public ProjectResponse.ProjectDto registerProject(ProjectRequest.ResisterDto request) {
        if (StringUtils.isBlank(request.getName())) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        List<Organization> organizations = getOrganizations(request.getDeveloperOrgId(), request.getCustomerOrgId());
        List<Member> members = getMembers(request.getMembers());

        Project project = projectRepository.save(ProjectRequest.ResisterDto.toEntity(request, organizations, members));

        List<ProgressStep> steps = new ArrayList<>();

        for (ProgressStep.CurrentStep step : ProgressStep.CurrentStep.values()) {
            ProgressStep progressStep = ProgressStep.builder()
                    .projectId(project.getId())
                    .name(step.getDescription())
                    .build();

            steps.add(progressStep);
        }

        progressStepRepository.saveAll(steps);

        Long firstStepId = steps.get(0).getId();

        project.updateProgressStep(firstStepId);

        return ProjectResponse.ProjectDto.toDto(project);
    }

    @Override
    public ProjectResponse.ProjectDto deleteProject(Long projectId) {

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        project.deleteProject();

        return ProjectResponse.ProjectDto.toDto(projectRepository.save(project));
    }

    @Override
    public ProjectResponse.ProjectDto updateProject(Long projectId,
        ProjectRequest.UpdateDto request) {
        if (StringUtils.isBlank(request.getName())) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        List<Organization> organizations = getOrganizations(request.getDeveloperOrgId(),
            request.getCustomerOrgId());
        List<Member> members = getMembers(request.getMembers());

        project = projectRepository.save(
            ProjectRequest.UpdateDto.toEntity(request, project, organizations, members));

        return ProjectResponse.ProjectDto.toDto(project);
    }


    @Override
    public ProjectResponse.ProjectListDto findAllProjects(String keyword, String status, int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage-1, pageSize, Sort.Direction.DESC, "id");
        Member member = currentMemberUtil.getCurrentMember();

        Page<ProjectResponse.ProjectListDetailDto> results = getProjectListByRoleAndType(member, keyword, status, pageable);

        return ProjectResponse.ProjectListDto.fromEntityPage(results);
    }

    @Override
    public ProjectResponse.ProjectManagementStepCountDto countProjectsByManagementStep() {
        Member member = currentMemberUtil.getCurrentMember();
        Member.Role role = member.getRole();

        List<ProjectCountByManagementStep> list = getProjectCountByRoleAndType(role, member);

        Map<String, Long> projectCountMap = list.stream()
                .collect(Collectors.toMap(
                        ProjectCountByManagementStep::getManagementStep,
                        ProjectCountByManagementStep::getCount
                ));

        return ProjectResponse.ProjectManagementStepCountDto.toDto(projectCountMap);
    }

    @Override
    public ProjectResponse.ProjectDetailDto findProjectByProjectId(Long projectId) {
        ProjectDetailsDto detailsDto = projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        return ProjectResponse.ProjectDetailDto.toDetailDto(detailsDto);
    }

    public ProjectResponse.ProjectUpdateDto getUpdateProjectInfo(Long projectId) {
        ProjectUpdateDetailsDto dto = projectRepository.getUpdateProjectInfoById(projectId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        List<Long> memberList = projectRepository.findProjectMemberListByProjectIdAndOrgId(projectId);

        return ProjectResponse.ProjectUpdateDto.toDto(dto, memberList);
    }

    private List<Organization> getOrganizations(Long developerOrgId, Long customerOrgId) {
        return Arrays.asList(
                organizationRepository.findByIdAndType(developerOrgId, Organization.Type.DEVELOPER)
                        .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND)),
                organizationRepository.findByIdAndType(customerOrgId, Organization.Type.CUSTOMER)
                        .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND))
        );
    }

    private List<Member> getMembers(List<Long> memberIds) {
        return memberIds.stream()
                .map(memberId -> memberRepository.findById(memberId)
                        .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND)))
                .collect(Collectors.toList());
    }

    private Page<ProjectResponse.ProjectListDetailDto> getProjectListByRoleAndType(Member member, String keyword, String status, Pageable pageable) {
        Member.Role role = member.getRole();

        if (role.equals(Member.Role.ADMIN)) {
            Page<Object[]> results = projectRepository.findAdminProjectsByKeywordAndStatus(keyword, status, pageable);
            return toProjectListDetailDto(results);
        }

        Organization.Type type = member.getOrganization().getType();
        Long memberId = member.getId();

        if (type == Organization.Type.DEVELOPER) {
            Page<Object[]> results = projectRepository.findDeveloperProjectsByKeywordAndStatus(keyword, status, pageable, memberId);
            return toProjectListDetailDto(results);
        } else if (type == Organization.Type.CUSTOMER) {
            Page<Object[]> results = projectRepository.findCustomerProjectsByKeywordAndStatus(keyword, status, pageable, memberId);
            return toProjectListDetailDto(results);
        }

        return Page.empty(pageable);
    }

    public Page<ProjectResponse.ProjectListDetailDto> toProjectListDetailDto(Page<Object[]> projectList) {
        return projectList.map(row -> new ProjectResponse.ProjectListDetailDto(
                ((Number) row[0]).longValue(), // id
                (String) row[1], // name
                (String) row[2], // description
                (String) row[3], // detail
                (String) row[4], // status
                (String) row[5], // managementStep
                (Date) row[6], // regAt
                (Date) row[7], // updateAt
                (Date) row[8], // startAt
                (Date) row[9], // closeAt
                (String) row[10], // deletedYn
                ((Number) row[11]).longValue(), // devOwnerId
                (String) row[12], // developerName
                (String) row[13], // customerName
                ((Number) row[14]).intValue() // clickable
        ));
    }

    private List<ProjectCountByManagementStep> getProjectCountByRoleAndType(Member.Role role, Member member) {
        if (role == Member.Role.ADMIN) {
            return projectRepository.countProjectsByManagementStep(null, null);
        }

        Organization.Type type = Optional.ofNullable(member.getOrganization())
                .map(Organization::getType)
                .orElse(null);

        if (role == Member.Role.MEMBER) {
            return switch (type) {
                case DEVELOPER ->
                        projectRepository.countProjectsByManagementStep(member.getOrganization().getId(), null);
                case CUSTOMER ->
                        projectRepository.countProjectsByManagementStep(null, member.getId());
            };
        }

        return Collections.emptyList();
    }

    @Override
    public ProjectResponse.ProjectListByManagementStepDto findProjectsByManagementSteps(String managementStep, int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage-1, pageSize, Sort.Direction.DESC, "id");
        Member member = currentMemberUtil.getCurrentMember();
        Page<ProjectListInfoByManagementStep> projectList = null;
        List<Long> clickableList = null;

        switch (member.getRole()) {
            case ADMIN:
                projectList = projectRepository.findProjectsByManagementSteps(null, null, managementStep, pageable);
                break;
            case MEMBER:
                Organization.Type orgType = member.getOrganization().getType();
                if (Organization.Type.DEVELOPER.equals(orgType)) {
                    projectList = projectRepository.findProjectsByManagementSteps(member.getOrganization().getId(), null, managementStep, pageable);
                    clickableList = projectRepository.memberByProject(member.getId(), pageable);
                } else if (Organization.Type.CUSTOMER.equals(orgType)) {
                    projectList = projectRepository.findProjectsByManagementSteps(null, member.getId(), managementStep, pageable);
                }
                break;
        }

        return createProjectManagementStepDtos(projectList, clickableList);
    }

    private ProjectResponse.ProjectListByManagementStepDto createProjectManagementStepDtos(Page<ProjectListInfoByManagementStep> projectList, List<Long> clickableList){
        Page<ProjectResponse.ProjectByManagementStepDto> results = projectList.map(project -> {
            int clickable = (clickableList == null || clickableList.contains(project.getId())) ? 1 : 0;
            return new ProjectResponse.ProjectByManagementStepDto(project.getId(), project.getName(), clickable);
        });

        return ProjectResponse.ProjectListByManagementStepDto.toDto(results);
    }

    private Page<ProjectResponse.ProjectListDetailDto> getProjectListByRoleAndType(Member member, String keyword, String status, Pageable pageable) {
        Member.Role role = member.getRole();

        if (role.equals(Member.Role.ADMIN)) {
            Page<Object[]> results = projectRepository.findAdminProjectsByKeywordAndStatus(keyword, status, pageable);
            return toProjectListDetailDto(results);
        }

        Organization.Type type = member.getOrganization().getType();
        Long memberId = member.getId();

        if (type == Organization.Type.DEVELOPER) {
            Page<Object[]> results = projectRepository.findDeveloperProjectsByKeywordAndStatus(keyword, status, pageable, memberId);
            return toProjectListDetailDto(results);
        } else if (type == Organization.Type.CUSTOMER) {
            Page<Object[]> results = projectRepository.findCustomerProjectsByKeywordAndStatus(keyword, status, pageable, memberId);
            return toProjectListDetailDto(results);
        }

        return Page.empty(pageable);
    }

    public Page<ProjectResponse.ProjectListDetailDto> toProjectListDetailDto(Page<Object[]> projectList) {
        return projectList.map(row -> new ProjectResponse.ProjectListDetailDto(
                ((Number) row[0]).longValue(), // id
                (String) row[1], // name
                (String) row[2], // description
                (String) row[3], // detail
                (String) row[4], // status
                (String) row[5], // managementStep
                (Date) row[6], // regAt
                (Date) row[7], // updateAt
                (Date) row[8], // startAt
                (Date) row[9], // closeAt
                (String) row[10], // deletedYn
                ((Number) row[11]).longValue(), // devOwnerId
                (String) row[12], // developerName
                (String) row[13], // customerName
                ((Number) row[14]).intValue() // clickable
        ));
    }

    private List<ProjectCountByManagementStep> getProjectCountByRoleAndType(Member.Role role, Member member) {
        if (role == Member.Role.ADMIN) {
            return projectRepository.countProjectsByManagementStep(null, null);
        }

        Organization.Type type = Optional.ofNullable(member.getOrganization())
                .map(Organization::getType)
                .orElse(null);

        if (role == Member.Role.MEMBER) {
            return switch (type) {
                case DEVELOPER ->
                        projectRepository.countProjectsByManagementStep(member.getOrganization().getId(), null);
                case CUSTOMER -> projectRepository.countProjectsByManagementStep(null, member.getId());
            };
        }

        return Collections.emptyList();
    }

}