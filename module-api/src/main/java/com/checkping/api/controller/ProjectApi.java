package com.checkping.api.controller;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.project.ProgressStepGet;
import com.checkping.dto.project.ProgressStepPlanUpdate;
import com.checkping.dto.project.ProjectRequest;
import com.checkping.dto.project.ProjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@Tag(name = "Project API(ProjectController)", description = "프로젝트 API 입니다.")
public interface ProjectApi {

    @Operation(summary = "프로젝트 생성", description = "프로젝트를 생성하는 기능입니다.")
    BaseResponse<ProjectResponse.ProjectDto> resisterProjects(
            @Parameter(description = "생성할 프로젝트 정보 Dto") ProjectRequest.ResisterDto request
    );

    @Operation(summary = "프로젝트 삭제", description = "프로젝트를 삭제하는 기능입니다.")
    BaseResponse<ProjectResponse.ProjectDto> deleteProjects(
            @Parameter(description = "프로젝트 ID") Long projectId
    );

    @Operation(summary = "프로젝트 조회", description = "프로젝트를 수정을 위한 정보를 조회하는 기능입니다.")
    BaseResponse<ProjectResponse.ProjectUpdateDto> getProjectUpdateInfo(
            @Parameter(description = "프로젝트 ID") Long projectId
    );

    @Operation(summary = "프로젝트 수정", description = "프로젝트를 수정하는 기능입니다.")
    BaseResponse<ProjectResponse.ProjectDto> updateProjects(
            @Parameter(description = "프로젝트 ID") Long projectId,
            @Parameter(description = "수정할 프로젝트 정보 Dto") ProjectRequest.UpdateDto request
    );

    @Operation(summary = "프로젝트 전체 목록", description = "프로젝트 전체 목록을 조회하는 기능입니다.")
    BaseResponse<ProjectResponse.ProjectListDto> listProjects(
            @Parameter(description = "프로젝트 상태") String status,
            @Parameter(description = "프로젝트 검색어") String keyword,
            @Parameter(description = "페이지 번호") int currentPage,
            @Parameter(description = "페이지 사이즈") int pageSize
    );

    @Operation(summary = "프로젝트 관리단계 별 개수 조회", description = "프로젝트 관리단계 별 개수를 조회하는 기능입니다.")
    BaseResponse<ProjectResponse.ProjectManagementStepCountDto> countProjectsByManagementStep();

    @Operation(summary = "프로젝트 별 정보 조회", description = "프로젝트의 기본 정보를 조회하는 기능입니다.")
    BaseResponse<ProjectResponse.ProjectInfoDto> getProject(
            @Parameter(description = "프로젝트 ID") Long projectId
    );

    @Operation(summary = "프로젝트 관리단계 별 리스트", description = "특정 관리단계 별 프로젝트 리스트를 조회하는 기능입니다.")
    BaseResponse<ProjectResponse.ProjectListByManagementStepDto> findProjectsByManagementSteps(
            @Parameter(description = "프로젝트 관리단계") String managementStep,
            @Parameter(description = "페이지 번호") int currentPage,
            @Parameter(description = "페이지 사이즈") int pageSize
    );

    @Operation(summary = "프로젝트 진행 상태 정보 조회", description = "프로젝트 진행 상태 정보를 조회하는 기능입니다.")
    BaseResponse<List<ProgressStepGet.Response>> getProgressStep(
            @Parameter(description = "프로젝트 ID") Long projectId
    );

    @Operation(summary = "프로젝트 진행 상태 일정 수정", description = "프로젝트 진행 상태의 일정을 수정하는 기능입니다.")
    BaseResponse<ProgressStepPlanUpdate.Response> updateProgressStepPlan(
            @Parameter(description = "프로젝트 ID") Long projectId,
            @Parameter(description = "프로젝트 진행 상태 ID") Long progressStepId,
            @Parameter(description = "수정할 프로젝트 진행 상태 일정 정보 Dto") ProgressStepPlanUpdate.Request request
    );
}
