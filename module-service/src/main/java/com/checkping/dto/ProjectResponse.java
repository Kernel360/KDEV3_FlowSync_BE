package com.checkping.dto;


import com.checkping.domain.project.Project;
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
        private String name;
        private String description;
        private String detail;
        private Project.Status status;
        private LocalDateTime regAt;
        private LocalDateTime updateAt;
        private LocalDateTime closeAt;
        private Long resisterId;
        private Long updaterId;
        private String deletedYn;

        private String developerType;
        private String developerName;
        private String customerType;
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
            projectDto.setCloseAt(project.getCloseAt());
            projectDto.setResisterId(project.getResisterId());
            projectDto.setUpdaterId(project.getUpdaterId());
            projectDto.setDeletedYn(project.getDeletedYn());
            return projectDto;
        }

        public void setOrganizationInfo(String developerType, String developerName, String customerType, String customerName) {
            this.developerType = developerType;
            this.developerName = developerName;
            this.customerType = customerType;
            this.customerName = customerName;
        }
    }

}