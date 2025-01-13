package com.checkping.service.project;

import com.checkping.dto.ProjectRequest;
import com.checkping.dto.ProjectResponse;

import java.util.List;

public interface ProjectService {
    ProjectResponse.ProjectDto registerProject(ProjectRequest.ResisterDto request);

    ProjectResponse.ProjectDto deleteProject(Long projectId);

    ProjectResponse.ProjectDto updateProject(Long projectId, ProjectRequest.UpdateDto request);

    List<ProjectResponse.ProjectDto> findAllProjects(String keyword, String status);
}