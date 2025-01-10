package com.checkping.api.controller;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.ProjectResponse;
import com.checkping.service.project.ProjectServiceImpl;
import com.checkping.dto.ProjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProjectController {

    @Autowired
    private ProjectServiceImpl projectService;

    @PostMapping("/projects")
    public BaseResponse<ProjectResponse.ProjectDto> resisterProjects(@RequestBody ProjectRequest.ResisterDto request) {
        ProjectResponse.ProjectDto projectDto = projectService.registerProject(request);
        log.info("FlowSync - resisterProjects name : {}, register_at : {}, resister_id : {}", projectDto.getName(), projectDto.getRegAt(), projectDto.getResisterId());
        return BaseResponse.success(projectDto);
    }
}