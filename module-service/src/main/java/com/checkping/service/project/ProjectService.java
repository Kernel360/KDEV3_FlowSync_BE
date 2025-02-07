package com.checkping.service.project;

import com.checkping.dto.project.ProjectRequest;
import com.checkping.dto.project.ProjectResponse;

import java.util.List;
import java.util.Map;

public interface ProjectService {
    ProjectResponse.ProjectDto registerProject(ProjectRequest.ResisterDto request);

    ProjectResponse.ProjectDto deleteProject(Long projectId);

    ProjectResponse.ProjectDto updateProject(Long projectId, ProjectRequest.UpdateDto request);

    ProjectResponse.ProjectListDto findAllProjects(String keyword, String status, int page, int size);

    ProjectResponse.ProjectManagementStepCountDto countProjectsByManagementStep();

    ProjectResponse.ProjectDetailDto findProjectByProjectId(Long projectId);

    ProjectResponse.ProjectInfoListDto getProjectInfoListByStatus();
}