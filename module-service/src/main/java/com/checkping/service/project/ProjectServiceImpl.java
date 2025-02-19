package com.checkping.service.project;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import com.checkping.domain.project.ProgressStep;
import com.checkping.domain.project.Project;
import com.checkping.domain.project.projection.OwnerInfo;
import com.checkping.domain.project.projection.ProjectCountByManagementStep;
import com.checkping.domain.project.projection.ProjectInfo;
import com.checkping.domain.project.projection.ProjectListInfoByManagementStep;
import com.checkping.dto.project.ProjectResponse;
import com.checkping.dto.project.ProjectSearchRequest;
import com.checkping.exception.member.MemberNotFoundException;
import com.checkping.exception.member.OrganizationNotFoundEntityException;
import com.checkping.exception.project.*;
import com.checkping.infra.dto.ProjectUpdateDetailsDto;
import com.checkping.infra.repository.member.MemberRepository;
import com.checkping.infra.repository.member.OrganizationRepository;
import com.checkping.infra.repository.project.ProgressStepRepository;
import com.checkping.infra.repository.project.ProjectRepository;
import com.checkping.dto.project.ProjectRequest;

import com.checkping.service.member.util.CurrentMemberUtil;
import io.micrometer.common.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        validateProjectData(request.getStartAt(), request.getDeadlineAt(), request.getManagementStep());

        List<Organization> organizations = getOrganizations(request.getDeveloperOrgId(), request.getCustomerOrgId());
        List<Member> members = getMembers(request.getMembers());

        Project project = projectRepository.save(ProjectRequest.ResisterDto.toEntity(request, organizations, members));

        List<ProgressStep> steps = Arrays.stream(ProgressStep.CurrentStep.values())
                .map(step -> ProgressStep.generate(project.getId(), step.getName(), step.getDescription(), step.getOrder()))
                .toList();

        if (steps.isEmpty()) {
            throw new ProjectStepCreationException();
        }

        progressStepRepository.saveAll(steps);

        if(project.getManagementStep().equals(Project.ManagementStep.COMPLETED)){
            project.updateCloseAt();
        }

        Long firstStepId = steps.get(0).getId();

        project.updateProgressStep(firstStepId);

        return ProjectResponse.ProjectDto.toDto(project);
    }

    @Override
    @Transactional
    public ProjectResponse.ProjectDto deleteProject(Long projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        if ("Y".equalsIgnoreCase(project.getDeletedYn())) {
            throw new ProjectAlreadyDeletedException();
        }

        project.deleteProject();

        return ProjectResponse.ProjectDto.toDto(projectRepository.save(project));
    }

    @Override
    @Transactional
    public ProjectResponse.ProjectDto updateProject(Long projectId,
                                                    ProjectRequest.UpdateDto request) {
        validateProjectData(request.getStartAt(), request.getDeadlineAt(), request.getManagementStep());

        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        List<Organization> organizations = getOrganizations(request.getDeveloperOrgId(), request.getCustomerOrgId());
        List<Member> members = getMembers(request.getMembers());

        if (Project.ManagementStep.valueOf(request.getManagementStep()) == Project.ManagementStep.COMPLETED) {
            project.updateCloseAt();
        }

        project = projectRepository.save(
                ProjectRequest.UpdateDto.toEntity(request, project, organizations, members));

        return ProjectResponse.ProjectDto.toDto(project);
    }


    @Override
    public ProjectResponse.ProjectListDto findAllProjects(ProjectSearchRequest searchRequest) {
        Pageable pageable = PageRequest.of(
                searchRequest.getCurrentPage(),
                searchRequest.getPageSize(),
                Sort.Direction.valueOf(searchRequest.getOrder()),
                searchRequest.getSort());
        Member member = currentMemberUtil.getCurrentMember();

        Page<ProjectResponse.ProjectListDetailDto> results = getProjectListByRoleAndType(member, searchRequest.getKeyword(), searchRequest.getManagementStep(), pageable);

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
    public ProjectResponse.ProjectInfoDto findProjectByProjectId(Long projectId) {
        ProjectInfo projectInfo = projectRepository.findProjectInfoById(projectId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        OwnerInfo developerOwnerInfo = projectRepository.findOwnerMemberInfoById(projectInfo.getDeveloperOwnerId()).orElse(null);
        OwnerInfo customerOwnerInfo = projectRepository.findOwnerMemberInfoById(projectInfo.getCustomerOwnerId()).orElse(null);
        // 추후 업체, 멤버가 완전 삭제될 시에도 프로젝트 정보를 가져올 수 있어야 하기 때문에 업체, 멤버에 한해 exception 처리를 제거

        return ProjectResponse.ProjectInfoDto.toDto(projectInfo, developerOwnerInfo, customerOwnerInfo);
    }

    @Transactional(readOnly = true)
    public ProjectResponse.ProjectUpdateDto getUpdateProjectInfo(Long projectId) {
        ProjectUpdateDetailsDto dto = projectRepository.getUpdateProjectInfoById(projectId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        List<Long> memberList = projectRepository.findProjectMemberListByProjectIdAndOrgId(projectId);

        return ProjectResponse.ProjectUpdateDto.toDto(dto, memberList);
    }

    private List<Organization> getOrganizations(Long developerOrgId, Long customerOrgId) {
        return Arrays.asList(
                organizationRepository.findByIdAndType(developerOrgId, Organization.Type.DEVELOPER)
                        .orElseThrow(OrganizationNotFoundEntityException::new),
                organizationRepository.findByIdAndType(customerOrgId, Organization.Type.CUSTOMER)
                        .orElseThrow(OrganizationNotFoundEntityException::new)
        );
    }

    private List<Member> getMembers(List<Long> memberIds) {
        return memberIds.stream()
                .map(memberId -> memberRepository.findById(memberId)
                        .orElseThrow(MemberNotFoundException::new))
                .collect(Collectors.toList());
    }

    private Page<ProjectResponse.ProjectListDetailDto> getProjectListByRoleAndType(Member member, String keyword, String managementStep, Pageable pageable) {
        Member.Role role = member.getRole();

        if (role.equals(Member.Role.ADMIN)) {
            Page<Object[]> results = projectRepository.findAdminProjectsByKeywordAndManagementStep(keyword, managementStep, pageable);
            return toProjectListDetailDto(results);
        }

        Organization.Type type = member.getOrganization().getType();
        Long memberId = member.getId();

        if (type == Organization.Type.DEVELOPER) {
            Page<Object[]> results = projectRepository.findDeveloperProjectsByKeywordAndManagementStep(keyword, managementStep, pageable, memberId);
            return toProjectListDetailDto(results);
        } else if (type == Organization.Type.CUSTOMER) {
            Page<Object[]> results = projectRepository.findCustomerProjectsByKeywordAndManagementStep(keyword, managementStep, pageable, memberId);
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
                (String) row[4], // managementStep
                (Date) row[5], // regAt
                (Date) row[6], // updateAt
                (Date) row[7], // startAt
                (Date) row[8], // deadlineAt
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

    @Transactional
    public ProjectResponse.ProjectDto updateManagementStep(Long projectId, String managementStep) {
        validateManagementStep(managementStep);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        Member member = currentMemberUtil.getCurrentMember();
        validateUpdatePermissions(member, project);

        Optional.of(Project.ManagementStep.valueOf(managementStep))
                .filter(step -> step == Project.ManagementStep.COMPLETED)
                .ifPresent(step -> project.updateCloseAt());

        if (Project.ManagementStep.valueOf(managementStep) == Project.ManagementStep.COMPLETED) {
            project.updateCloseAt();
        }

        project.updateManagementStep(Project.ManagementStep.valueOf(managementStep));

        return ProjectResponse.ProjectDto.toDto(project);
    }

    private void validateProjectData(LocalDateTime startAt, LocalDateTime deadlineAt, String managementStep) {
        if (!deadlineAt.isAfter(startAt)) {
            throw new InvalidProjectDataException("마감일은 시작일보다 이후여야 합니다.");
        }

        validateManagementStep(managementStep);
    }

    private void validateManagementStep(String managementStep) {
        if (StringUtils.isBlank(managementStep))
            throw new InvalidProjectDataException("관리 단계는 필수 입력값입니다.");

        try {
            Project.ManagementStep.valueOf(managementStep);
        } catch (IllegalArgumentException e) {
            throw new InvalidProjectDataException("잘못된 관리 단계 값입니다.");
        }
    }

    private void validateUpdatePermissions(Member member, Project project) {
        if (member.getRole() == Member.Role.ADMIN) return;

        if (member.getOrganization() == null
                || member.getOrganization().getType() != Organization.Type.DEVELOPER
                || !member.getId().equals(project.getDevOwner().getId())) {
            throw new ProjectUpdatePermissionException();
        }
    }
}