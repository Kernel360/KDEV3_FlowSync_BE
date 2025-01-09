package com.checkping.service.project;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.CustomException;
import com.checkping.domain.project.Project;
import com.checkping.dto.ProjectResponse;
import com.checkping.infra.repository.ProjectRepository;
import com.checkping.dto.ProjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public ProjectResponse.ProjectDto registerProject(ProjectRequest.ProjecResisterDto request) {
        if(request == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
        Project project = projectRepository.save(ProjectRequest.ProjecResisterDto.toEntity(request));
        return ProjectResponse.ProjectDto.toDto(project);
    }
}
