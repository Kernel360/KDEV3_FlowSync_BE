package com.checkping.dto;


import com.checkping.domain.project.Project;
import com.checkping.infra.repository.project.ProjectDetailsDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class ProjectResponse {

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectDto {
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
        private UUID resisterId;
        private UUID updaterId;
        private String deletedYn;
        private UUID devOwnerId;
        private String developerName;
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
                    .resisterId(project.getResisterId())
                    .updaterId(project.getUpdaterId())
                    .deletedYn(project.getDeletedYn())
                    .devOwnerId(project.getDevOwner().getId())
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
        private Long id;
        private String projectName;
        private String description;
        private String devOrgName;
        private String profileImageUrl;
        private String memberName;
        private String jobRole;
        private String phoneNum;
        private Date startAt;
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


}