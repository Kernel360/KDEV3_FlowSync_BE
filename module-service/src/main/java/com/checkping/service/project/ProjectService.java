package com.checkping.service.project;

import com.checkping.dto.project.ProjectRequest;
import com.checkping.dto.project.ProjectResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProjectService {
    ProjectResponse.ProjectDto registerProject(ProjectRequest.ResisterDto request);

    ProjectResponse.ProjectDto deleteProject(Long projectId);

    ProjectResponse.ProjectDto updateProject(Long projectId, ProjectRequest.UpdateDto request);

    ProjectResponse.ProjectListDto findAllProjects(String keyword, String status, Pageable pageable);

    Map<String, Long> countProjectsByManagementStep();

    ProjectResponse.ProjectDetailDto findProjectByProjectId(Long projectId);

    Map<String, List<ProjectResponse.ProjectInfoDto>> getProjectInfoListByStatus();
}