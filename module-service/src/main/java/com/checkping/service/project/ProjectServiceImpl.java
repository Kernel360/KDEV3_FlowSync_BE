package com.checkping.service.project;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import com.checkping.domain.project.ProgressStep;
import com.checkping.domain.project.Project;
import com.checkping.dto.project.ProjectResponse;
import com.checkping.infra.dto.ProjectUpdateDetailsDto;
import com.checkping.infra.repository.member.MemberRepository;
import com.checkping.infra.repository.member.OrganizationRepository;
import com.checkping.infra.repository.project.ProgressStepRepository;
import com.checkping.infra.dto.ProjectDetailsDto;
import com.checkping.infra.repository.project.projection.ProjectInfoProjection;
import com.checkping.infra.repository.project.ProjectRepository;
import com.checkping.dto.project.ProjectRequest;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                    .project_id(project.getId())
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
        if (projectId == null) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        Project updatedProject = project.toBuilder()
            .updateAt(LocalDateTime.now())
            .deletedYn("Y")
            .build();

        return ProjectResponse.ProjectDto.toDto(projectRepository.save(updatedProject));
    }

    public ProjectResponse.ProjectUpdateDto getUpdateProjectInfo(Long projectId) {
        ProjectUpdateDetailsDto dto = projectRepository.getUpdateProjectInfoById(projectId);
        List<Long> memberList = projectRepository.findProjectMemberListByProjectIdAndOrgId(projectId);

        return ProjectResponse.ProjectUpdateDto.toDto(dto, memberList);
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
    public ProjectResponse.ProjectListDto findAllProjects(String keyword, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page-1, size, Sort.Direction.DESC, "id");

        Page<Project> results = projectRepository.findProjectsWithOrganizationInfoByKeywordAndStatus(
            keyword, status, pageable);

        return ProjectResponse.ProjectListDto.fromEntityPage(results);
    }

    @Override
    public Map<String, Long> countProjectsByManagementStep() {
        List<Tuple> list = projectRepository.countProjectsByManagementStep();

        if (list.isEmpty()) {
            return Collections.emptyMap();
        }

        return list.stream()
                .collect(Collectors.toMap(
                        tuple -> ((Project.ManagementStep) tuple.get("managementStep")).name(),
                        tuple -> (Long) tuple.get("projectCount")
                ));
    }

    @Override
    public ProjectResponse.ProjectDetailDto findProjectByProjectId(Long projectId) {
        ProjectDetailsDto detailsDto = projectRepository.findProjectById(projectId);
        return ProjectResponse.ProjectDetailDto.toDetailDto(detailsDto);
    }

    @Override
    public ProjectResponse.ProjectInfoListDto getProjectInfoListByStatus(){

        List<ProjectInfoProjection> inProgressList = projectRepository.findByStatus(Project.Status.IN_PROGRESS);
        List<ProjectInfoProjection> completedList =projectRepository.findByStatus(Project.Status.COMPLETED);

        List<ProjectResponse.ProjectInfoDto> inProgressDTOList = new ArrayList<>();
        inProgressList.forEach(project ->
                inProgressDTOList.add(ProjectResponse.ProjectInfoDto.builder()
                        .id(project.getId())
                        .projectName(project.getName())
                        .build())
        );

        List<ProjectResponse.ProjectInfoDto> completedDTOList = new ArrayList<>();
        completedList.forEach(project ->
                completedDTOList.add(ProjectResponse.ProjectInfoDto.builder()
                        .id(project.getId())
                        .projectName(project.getName())
                        .build())
        );

        Map<String, List<ProjectResponse.ProjectInfoDto>> result = new HashMap<>();
        result.put("inProgressList", inProgressDTOList);
        result.put("completedList", completedDTOList);

        return ProjectResponse.ProjectInfoListDto.infoListDto(result);
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

}