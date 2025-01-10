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
            return projectDto;
        }
    }

}