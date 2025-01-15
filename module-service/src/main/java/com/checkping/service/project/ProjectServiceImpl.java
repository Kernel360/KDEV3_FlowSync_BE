package com.checkping.service.project;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.domain.project.Project;
import com.checkping.dto.ProjectResponse;
import com.checkping.infra.repository.project.ProjectRepository;
import com.checkping.dto.ProjectRequest;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public ProjectResponse.ProjectDto registerProject(ProjectRequest.ResisterDto request) {
        if (StringUtils.isBlank(request.getName())) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        Project project = projectRepository.save(ProjectRequest.ResisterDto.toEntity(request));
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
                    .getType().toString() : null, // developerType
                !project.getOrganizations().isEmpty() ? project.getOrganizations().get(0)
                    .getName() : null,            // developerName
                !project.getOrganizations().isEmpty() ? project.getOrganizations().get(1)
                    .getType().toString() : null, // customerType
                !project.getOrganizations().isEmpty() ? project.getOrganizations().get(1)
                    .getName() : null             // customerName
            );

            return projectDto;
        }).collect(Collectors.toList());
    }

}