package com.checkping.service.project;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import com.checkping.domain.project.Project;
import com.checkping.dto.ProjectResponse;
import com.checkping.infra.repository.member.MemberRepository;
import com.checkping.infra.repository.member.OrganizationRepository;
import com.checkping.infra.repository.project.ProjectRepository;
import com.checkping.dto.ProjectRequest;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public ProjectResponse.ProjectDto registerProject(ProjectRequest.ResisterDto request) {
        if (StringUtils.isBlank(request.getName())) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        List<Organization> organizations = getOrganizations(request.getDeveloperOrgId(), request.getCustomerOrgId());
        List<Member> members = getMembers(request.getMembers());

        Project project = projectRepository.save(ProjectRequest.ResisterDto.toEntity(request, organizations, members));
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

    @Override
    public ProjectResponse.ProjectDto updateProject(Long projectId,
        ProjectRequest.UpdateDto request) {
        if (StringUtils.isBlank(request.getName())) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        List<Organization> organizations = getOrganizations(request.getDeveloperOrgId(), request.getCustomerOrgId());
        List<Member> members = getMembers(request.getMembers());

        project = projectRepository.save(ProjectRequest.UpdateDto.toEntity(request, project, organizations, members));

        return ProjectResponse.ProjectDto.toDto(project);
    }


    @Override
    public List<ProjectResponse.ProjectDto> findAllProjects(String keyword, String status) {
        List<Project> results = projectRepository.findProjectsWithOrganizationInfoByKeywordAndStatus(
            keyword, status);

        return results.stream().map(result -> {
            Project project = result;

            ProjectResponse.ProjectDto projectDto = ProjectResponse.ProjectDto.toDto(project);

            return projectDto;
        }).collect(Collectors.toList());
    }

    private List<Organization> getOrganizations(String developerOrgId, String customerOrgId) {
        return Arrays.asList(
                organizationRepository.findById(UUID.fromString(developerOrgId))
                        .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND)),
                organizationRepository.findById(UUID.fromString(customerOrgId))
                        .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND))
        );
    }

    private List<Member> getMembers(List<String> memberIds) {
        return memberIds.stream()
                .map(memberId -> memberRepository.findById(UUID.fromString(memberId))
                        .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND)))
                .collect(Collectors.toList());
    }

}