package com.checkping.service.project;

import com.checkping.dto.ProjectRequest;
import com.checkping.dto.ProjectResponse;

public interface ProjectService {
    ProjectResponse.ProjectDto registerProject(ProjectRequest.ProjecResisterDto request);
}