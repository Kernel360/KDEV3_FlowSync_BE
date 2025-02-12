package com.checkping.service.project;

import com.checkping.dto.project.ProjectRequest;
import com.checkping.dto.project.ProjectResponse;
import com.checkping.dto.project.ProjectSearchRequest;

import java.util.List;
import java.util.Map;

public interface ProjectService {
    ProjectResponse.ProjectDto registerProject(ProjectRequest.ResisterDto request);

    ProjectResponse.ProjectDto deleteProject(Long projectId);

    ProjectResponse.ProjectDto updateProject(Long projectId, ProjectRequest.UpdateDto request);

    ProjectResponse.ProjectListDto findAllProjects(ProjectSearchRequest searchRequest);

    ProjectResponse.ProjectManagementStepCountDto countProjectsByManagementStep();

    ProjectResponse.ProjectInfoDto findProjectByProjectId(Long projectId);

    ProjectResponse.ProjectListByManagementStepDto findProjectsByManagementSteps(String managementStep, int currentPage, int pageSize);
}