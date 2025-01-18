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
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

        Organization devOrganization = organizationRepository.findById(UUID.fromString(request.getDeveloperOrgId()))
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        Organization custOrganization = organizationRepository.findById(UUID.fromString(request.getCustomerOrgId()))
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        List<Organization> organizations = new ArrayList<>();
        organizations.add(devOrganization);
        organizations.add(custOrganization);

        List<Member> members = request.getMembers().stream()
                .map(memberId -> memberRepository.findById(UUID.fromString(memberId))
                        .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND)))
                .collect(Collectors.toList());

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

        project = projectRepository.save(ProjectRequest.UpdateDto.toEntity(request, project));

        return ProjectResponse.ProjectDto.toDto(project);
    }


    @Override
    public List<ProjectResponse.ProjectDto> findAllProjects(String keyword, String status) {
        List<Project> results = projectRepository.findProjectsWithOrganizationInfoByKeywordAndStatus(
            keyword, status);

        return results.stream().map(result -> {
            Project project = result;

            ProjectResponse.ProjectDto projectDto = ProjectResponse.ProjectDto.toDto(project);

            // 추가된 정보인 개발사 및 고객사 정보를 설정
            projectDto.setOrganizationInfo(
                !project.getOrganizations().isEmpty() ? project.getOrganizations().get(0)
                    .getName() : null,            // developerName
                !project.getOrganizations().isEmpty() ? project.getOrganizations().get(1)
                    .getName() : null             // customerName
            );

            return projectDto;
        }).collect(Collectors.toList());
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

}