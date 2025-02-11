package com.checkping.api.controller;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.project.ProgressStepGet;
import com.checkping.dto.project.ProgressStepGet.Response;
import com.checkping.dto.project.ProgressStepPlanUpdate;
import com.checkping.dto.project.ProjectRequest;
import com.checkping.dto.project.ProjectResponse;
import com.checkping.service.project.ProjectServiceImpl;
import com.checkping.service.project.progressstep.ProgressStepService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProjectController implements ProjectApi {

    private final ProjectServiceImpl projectService;
    private final ProgressStepService progressStepService;

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
    @GetMapping("/admins/projects/{projectId}")
    public BaseResponse<ProjectResponse.ProjectUpdateDto> getProjectUpdateInfo(@PathVariable Long projectId) {
        ProjectResponse.ProjectUpdateDto projectUpdateDto = projectService.getUpdateProjectInfo(projectId);
        return BaseResponse.success(projectUpdateDto);
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
            @RequestParam(required = false) String managementStep,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize
            ) {

        ProjectResponse.ProjectListDto projects = projectService.findAllProjects(keyword, managementStep, currentPage, pageSize);
        //log.info("FlowSync - getProjectlist : ");
        return BaseResponse.success(projects);
    }

    @Override
    @GetMapping(value={"/admins/projects/management-steps/count", "/projects/management-steps/count"})
    public BaseResponse<ProjectResponse.ProjectManagementStepCountDto> countProjectsByManagementStep() {
        ProjectResponse.ProjectManagementStepCountDto projectCount = projectService.countProjectsByManagementStep();
        return BaseResponse.success(projectCount);
    }

    @Override
    @GetMapping(value = {"/admins/projects/{projectId}/project-info", "/projects/{projectId}/project-info"})
    public BaseResponse<ProjectResponse.ProjectInfoDto> getProject(@PathVariable Long projectId) {
        ProjectResponse.ProjectInfoDto project = projectService.findProjectByProjectId(projectId);
        return BaseResponse.success(project);
    }

    @Override
    @GetMapping(value = {"/admins/projects/management-steps", "/projects/management-steps"})
    public BaseResponse<ProjectResponse.ProjectListByManagementStepDto> findProjectsByManagementSteps(
            @RequestParam String managementStep,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize) {
        ProjectResponse.ProjectListByManagementStepDto projectList = projectService.findProjectsByManagementSteps(managementStep, currentPage, pageSize);
        return BaseResponse.success(projectList);
    }

    @Override
    @GetMapping("/projects/{projectId}/progress-steps")
    public BaseResponse<List<ProgressStepGet.Response>> getProgressStep(@PathVariable Long projectId) {

        List<Response> response = progressStepService.getProgressStep(projectId);

        return BaseResponse.success(response);
    }

    @Override
    @PutMapping("/projects/{projectId}/progress-steps/{progressStepId}/plans")
    public BaseResponse<ProgressStepPlanUpdate.Response> updateProgressStepPlan(@PathVariable Long projectId,
        @PathVariable Long progressStepId,
        @RequestBody @Valid ProgressStepPlanUpdate.Request request) {

        ProgressStepPlanUpdate.Response response = progressStepService.updateProgressStepPlan(projectId, progressStepId, request);

        return BaseResponse.success(response);
    }
}