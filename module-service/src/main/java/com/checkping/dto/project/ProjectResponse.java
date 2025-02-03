package com.checkping.dto.project;


import com.checkping.common.dto.PageMetaResponse;
import com.checkping.domain.project.Project;
import com.checkping.infra.dto.ProjectDetailsDto;
import com.checkping.infra.dto.ProjectListDetailsDto;
import com.checkping.infra.dto.ProjectUpdateDetailsDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProjectResponse {

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectDto {
        @Schema(description = "프로젝트 아이디")
        private Long id;
        @Schema(description = "프로젝트 이름")
        private String name;
        @Schema(description = "프로젝트 짧은 설명")
        private String description;
        @Schema(description = "프로젝트 긴 설명")
        private String detail;
        @Schema(description = "프로젝트 상태")
        private Project.Status status;
        @Schema(description = "프로젝트 관리 단계")
        private Project.ManagementStep managementStep;
        @Schema(description = "프로젝트 등록 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime regAt;
        @Schema(description = "프로젝트 수정 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updateAt;
        @Schema(description = "프로젝트 시작 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startAt;
        @Schema(description = "프로젝트 마감 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime closeAt;
        @Schema(description = "프로젝트 삭제여부")
        private String deletedYn;
        @Schema(description = "개발사 대표자 아이디")
        private Long devOwnerId;
        @Schema(description = "고객사 결재자 아이디")
        private Long customerOwnerId;
        @Schema(description = "개발사 이름")
        private String developerName;
        @Schema(description = "고객사 이름")
        private String customerName;

        public static ProjectDto toDto(Project project) {
            return ProjectDto.builder()
                    .id(project.getId())
                    .name(project.getName())
                    .description(project.getDescription())
                    .detail(project.getDetail())
                    .status(project.getStatus())
                    .managementStep(project.getManagementStep())
                    .regAt(project.getRegAt())
                    .updateAt(project.getUpdateAt())
                    .startAt(project.getStartAt())
                    .closeAt(project.getCloseAt())
                    .deletedYn(project.getDeletedYn())
                    .devOwnerId(project.getDevOwner().getId())
                    .customerOwnerId(project.getCustomerOwner().getId())
                    .developerName(project.getOrganizations().get(0).getName())
                    .customerName(project.getOrganizations().get(1).getName())
                    .build();
        }
    }

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectDetailDto {
        @Schema(description = "프로젝트 아이디")
        private Long id;
        @Schema(description = "프로젝트 이름")
        private String projectName;
        @Schema(description = "프로젝트 짧은 설명")
        private String description;
        @Schema(description = "개발사 이름")
        private String devOrgName;
        @Schema(description = "개발사 대표자 프로필 이미지 url")
        private String profileImageUrl;
        @Schema(description = "개발사 대표자 이름")
        private String memberName;
        @Schema(description = "개발사 대표자 직무")
        private String jobRole;
        @Schema(description = "개발사 대표자 직급")
        private String jobTitle;
        @Schema(description = "개발사 대표자 연락처")
        private String phoneNum;
        @Schema(description = "프로젝트 시작 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startAt;
        @Schema(description = "프로젝트 마감 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date closeAt;

        public static ProjectDetailDto toDetailDto(ProjectDetailsDto detailsDto) {
            return ProjectDetailDto.builder()
                    .id(detailsDto.getId())
                    .projectName(detailsDto.getProjectName())
                    .description(detailsDto.getDescription())
                    .devOrgName(detailsDto.getDevOrgName())
                    .profileImageUrl(detailsDto.getProfileImageUrl())
                    .memberName(detailsDto.getMemberName())
                    .jobRole(detailsDto.getJobRole())
                    .jobTitle(detailsDto.getJobTitle())
                    .phoneNum(detailsDto.getPhoneNum())
                    .startAt(detailsDto.getStartAt())
                    .closeAt(detailsDto.getCloseAt())
                    .build();
        }
    }

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectInfoDto {
        @Schema(description = "프로젝트 아이디")
        private Long id;
        @Schema(description = "프로젝트 이름")
        private String projectName;
    }

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectListDetailDto {
        @Schema(description = "프로젝트 아이디")
        private Long id;
        @Schema(description = "프로젝트 이름")
        private String name;
        @Schema(description = "프로젝트 짧은 설명")
        private String description;
        @Schema(description = "프로젝트 긴 설명")
        private String detail;
        @Schema(description = "프로젝트 상태")
        private Project.Status status;
        @Schema(description = "프로젝트 관리 단계")
        private Project.ManagementStep managementStep;
        @Schema(description = "프로젝트 등록 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date regAt;
        @Schema(description = "프로젝트 수정 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateAt;
        @Schema(description = "프로젝트 시작 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startAt;
        @Schema(description = "프로젝트 마감 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date closeAt;
        @Schema(description = "프로젝트 삭제여부")
        private String deletedYn;
        @Schema(description = "개발사 대표자 아이디")
        private Long devOwnerId;
        @Schema(description = "개발사 이름")
        private String developerName;
        @Schema(description = "고객사 이름")
        private String customerName;
        @Schema(description = "프로젝트 클릭 가능 여부")
        private Long clickable;

        public static ProjectListDetailDto toDto(ProjectListDetailsDto dto) {
            return ProjectListDetailDto.builder()
                    .id(dto.getId())
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .detail(dto.getDetail())
                    .status(Project.Status.valueOf(dto.getStatus()))
                    .managementStep(Project.ManagementStep.valueOf(dto.getManagementStep()))
                    .regAt(dto.getRegAt())
                    .updateAt(dto.getUpdateAt())
                    .startAt(dto.getStartAt())
                    .closeAt(dto.getCloseAt())
                    .deletedYn(dto.getDeletedYn())
                    .devOwnerId(dto.getDevOwnerId())
                    .developerName(dto.getDeveloperName())
                    .customerName(dto.getCustomerName())
                    .clickable(dto.getClickable())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectListDto {
        private List<ProjectListDetailDto> projects;
        private Map<String, Object> meta;

        public static ProjectListDto fromEntityPage(Page<ProjectListDetailsDto> page) {

            List<ProjectListDetailDto> projectDtos = page.getContent().stream()
                    .map(ProjectResponse.ProjectListDetailDto::toDto)
                    .toList();

            PageMetaResponse meta = PageMetaResponse.fromPage(page);
            Map<String, Object> result = meta.toMap();

            return new ProjectListDto(projectDtos, result);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectInfoListDto {
        private Map<String, List<ProjectInfoDto>> projectInfoMap;

        public static ProjectInfoListDto infoListDto(Map<String, List<ProjectInfoDto>> projectInfoMap) {
            return new ProjectInfoListDto(projectInfoMap);
        }
    }

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectUpdateDto {
        @Schema(description = "프로젝트 아이디")
        private Long id;
        @Schema(description = "프로젝트 이름")
        private String name;
        @Schema(description = "프로젝트 짧은 설명")
        private String description;
        @Schema(description = "프로젝트 긴 설명")
        private String detail;
        @Schema(description = "프로젝트 상태")
        private Project.Status status;
        @Schema(description = "프로젝트 관리 단계")
        private Project.ManagementStep managementStep;
        @Schema(description = "프로젝트 현재 진행단계 아이디")
        private Long progressStepId;
        @Schema(description = "프로젝트 시작 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date startAt;
        @Schema(description = "프로젝트 마감 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date closeAt;
        @Schema(description = "개발사 대표자 아이디")
        private Long devOwnerId;
        @Schema(description = "고객사 결재자 아이디")
        private Long customerOwnerId;
        @Schema(description = "개발사 아이디")
        private Long developerOrgId;
        @Schema(description = "고객사 아이디")
        private Long customerOrgId;
        @Schema(description = "프로젝트 멤버")
        private List<Long> members;

        public static ProjectUpdateDto toDto(ProjectUpdateDetailsDto detailsDto, List<Long> members) {
            return ProjectUpdateDto.builder()
                    .id(detailsDto.getId())
                    .name(detailsDto.getName())
                    .description(detailsDto.getDescription())
                    .detail(detailsDto.getDetail())
                    .status(Project.Status.valueOf(detailsDto.getStatus()))
                    .managementStep(Project.ManagementStep.valueOf(detailsDto.getManagementStep()))
                    .progressStepId(detailsDto.getProgressStepId())
                    .startAt(detailsDto.getStartAt())
                    .closeAt(detailsDto.getCloseAt())
                    .devOwnerId(detailsDto.getDevOwnerId())
                    .customerOwnerId(detailsDto.getCustomerOrgId())
                    .developerOrgId(detailsDto.getDeveloperOrgId())
                    .customerOrgId(detailsDto.getCustomerOrgId())
                    .members(members)
                    .build();
        }
    }
}