package com.checkping.domain.project.projection;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ProjectCountByManagementStep {

    private String managementStep;
    private long count;

    @QueryProjection
    public ProjectCountByManagementStep(String managementStep, long count) {
        this.managementStep = managementStep;
        this.count = count;
    }
}
