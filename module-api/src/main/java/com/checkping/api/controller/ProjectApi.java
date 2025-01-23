package com.checkping.api.controller;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.ProjectRequest;
import com.checkping.dto.ProjectResponse;
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

    @Operation(summary = "프로젝트 수정", description = "프로젝트를 수정하는 기능입니다.")
    BaseResponse<ProjectResponse.ProjectDto> updateProjects(
            @Parameter(description = "프로젝트 ID") Long projectId,
            @Parameter(description = "수정할 프로젝트 정보 Dto") ProjectRequest.UpdateDto request
    );

    @Operation(summary = "프로젝트 전체 목록", description = "프로젝트 전체 목록을 조회하는 기능입니다.")
    BaseResponse<List<ProjectResponse.ProjectDto>> listProjects(
            @Parameter(description = "프로젝트 상태") String status,
            @Parameter(description = "프로젝트 검색어") String keyword
    );

    @Operation(summary = "프로젝트 관리단계 별 개수 조회", description = "프로젝트 관리단계 별 개수를 조회하는 기능입니다.")
    BaseResponse<Map<String, Long>> countProjectsByManagementStep();

    @Operation(summary = "프로젝트 별 정보 조회", description = "프로젝트의 기본 정보를 조회하는 기능입니다.")
    BaseResponse<ProjectResponse.ProjectDetailDto> getProject(
            @Parameter(description = "프로젝트 ID") Long projectId
    );
}
