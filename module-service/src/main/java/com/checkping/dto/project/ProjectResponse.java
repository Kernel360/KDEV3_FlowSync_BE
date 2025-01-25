package com.checkping.dto.project;


import com.checkping.common.dto.PageMetaResponse;
import com.checkping.domain.project.Project;
import com.checkping.infra.dto.ProjectDetailsDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        @Schema(description = "개발사 대표자 아이디", example = "123e4567-e89b-12d3-a456-426614174000")
        private UUID devOwnerId;
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
                    .managementStep(project.getManagement_step())
                    .regAt(project.getRegAt())
                    .updateAt(project.getUpdateAt())
                    .startAt(project.getStartAt())
                    .closeAt(project.getCloseAt())
                    .deletedYn(project.getDeletedYn())
                    /*.devOwnerId(project.getDevOwner().getId())*/
                    /*.developerName(project.getOrganizations().get(0).getName())
                    .customerName(project.getOrganizations().get(1).getName())*/
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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectListDto {
        private List<ProjectDto> projects;
        private Map<String, Object> meta;


        public static ProjectListDto fromEntityPage(Page<Project> page) {
            List<ProjectResponse.ProjectDto> projectDtos = page.getContent().stream()
                    .map(ProjectResponse.ProjectDto::toDto)
                    .toList();

            PageMetaResponse meta = PageMetaResponse.fromPage(page);
            Map<String, Object> result =  meta.toMap();

            return new ProjectListDto(projectDtos, result);
        }
    }
}