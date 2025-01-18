package com.checkping.dto;


import com.checkping.domain.project.Project;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

public class ProjectResponse {

    @Getter
    @Setter
    @ToString
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
        private Long resisterId;
        private Long updaterId;
        private String deletedYn;

        private String developerName;
        private String customerName;

        public static ProjectDto toDto(Project project) {
            ProjectDto projectDto = new ProjectDto();
            projectDto.setId(project.getId());
            projectDto.setName(project.getName());
            projectDto.setDescription(project.getDescription());
            projectDto.setDetail(project.getDetail());
            projectDto.setStatus(project.getStatus());
            projectDto.setRegAt(project.getRegAt());
            projectDto.setUpdateAt(project.getUpdateAt());
            projectDto.setStartAt(project.getStartAt());
            projectDto.setCloseAt(project.getCloseAt());
            projectDto.setResisterId(project.getResisterId());
            projectDto.setUpdaterId(project.getUpdaterId());
            projectDto.setDeletedYn(project.getDeletedYn());
            projectDto.setDeveloperName(project.getOrganizations().get(0).getName());
            projectDto.setCustomerName(project.getOrganizations().get(1).getName());
            return projectDto;
        }
    }

}