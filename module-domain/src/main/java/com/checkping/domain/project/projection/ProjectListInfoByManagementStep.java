package com.checkping.domain.project.projection;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class ProjectListInfoByManagementStep {

    private long id;
    private String name;

    @QueryProjection
    public ProjectListInfoByManagementStep(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
