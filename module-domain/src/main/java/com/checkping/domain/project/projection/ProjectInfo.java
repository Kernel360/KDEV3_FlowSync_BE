package com.checkping.domain.project.projection;

import com.checkping.domain.project.Project;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProjectInfo {
    private Long id;
    private String projectName;
    private String description;
    private Project.ManagementStep managementStep;
    private Long developerOwnerId;
    private Long customerOwnerId;
    private LocalDateTime startAt;
    private LocalDateTime closeAt;

    @QueryProjection
    public ProjectInfo(Long id, String projectName, String description, Project.ManagementStep managementStep, Long developerOwnerId, Long customerOwnerId, LocalDateTime startAt, LocalDateTime closeAt) {
        this.id = id;
        this.projectName = projectName;
        this.description = description;
        this.managementStep = managementStep;
        this.developerOwnerId = developerOwnerId;
        this.customerOwnerId = customerOwnerId;
        this.startAt = startAt;
        this.closeAt = closeAt;
    }
}
