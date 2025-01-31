package com.checkping.api.controller;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.project.ProjectResponse;
import com.checkping.service.project.ProjectServiceImpl;
import com.checkping.dto.project.ProjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProjectController implements ProjectApi {

    private final ProjectServiceImpl projectService;

    @Override
    @PostMapping("/admins/projects")
    public BaseResponse<ProjectResponse.ProjectDto> resisterProjects(@RequestBody ProjectRequest.ResisterDto request) {
        ProjectResponse.ProjectDto projectDto = projectService.registerProject(request);
        log.info("FlowSync - resisterProjects name : {}, register_at : {}", projectDto.getName(), projectDto.getRegAt());
        return BaseResponse.success(projectDto);
    }

    @Override
    @DeleteMapping("/admins/projects/{projectId}")
    public BaseResponse<ProjectResponse.ProjectDto> deleteProjects(@PathVariable Long projectId) {
        ProjectResponse.ProjectDto projectDto = projectService.deleteProject(projectId);
        log.info("FlowSync - deleteProjects project_id : {}, ", projectId);
        return BaseResponse.success(projectDto);
    }

    @Override
    @PatchMapping("/admins/projects/{projectId}")
    public BaseResponse<ProjectResponse.ProjectDto> updateProjects(
            @PathVariable Long projectId,
            @RequestBody ProjectRequest.UpdateDto request)
    {
        ProjectResponse.ProjectDto projectDto = projectService.updateProject(projectId, request);
        log.info("FlowSync - updateProjects project_id : {}, name : {}, update_at : {}", projectDto.getId(), projectDto.getName(), projectDto.getUpdateAt());
        return BaseResponse.success(projectDto);
    }

    @Override
    @GetMapping(value = {"/admins/projects", "/projects"})
    public BaseResponse<ProjectResponse.ProjectListDto> listProjects(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize
            ) {

        ProjectResponse.ProjectListDto projects = projectService.findAllProjects(keyword, status, currentPage, pageSize);
        //log.info("FlowSync - getProjectlist : ");
        return BaseResponse.success(projects);
    }

    @Override
    @GetMapping("/admins/projects/management-steps")
    public BaseResponse<Map<String, Long>> countProjectsByManagementStep() {
        Map<String, Long> managementCountMap = projectService.countProjectsByManagementStep();
        return BaseResponse.success(managementCountMap);
    }

    @Override
    @GetMapping(value = {"/admins/projects/{projectId}/projectInfo", "/projects/{projectId}/projectInfo"})
    public BaseResponse<ProjectResponse.ProjectDetailDto> getProject(@PathVariable Long projectId) {
        ProjectResponse.ProjectDetailDto project = projectService.findProjectByProjectId(projectId);
        return BaseResponse.success(project);
    }

    @Override
    @GetMapping("/admins/projects/status")
    public BaseResponse<ProjectResponse.ProjectInfoListDto> listProjectInfoByStatus() {
        ProjectResponse.ProjectInfoListDto projectList = projectService.getProjectInfoListByStatus();
        return BaseResponse.success(projectList);
    }
}