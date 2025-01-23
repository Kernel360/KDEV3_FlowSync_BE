package com.checkping.infra.repository.project;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
public class ProjectDetailsDto {
    private final Long id;
    private final String projectName;
    private final String description;
    private final String devOrgName;
    private final String profileImageUrl;
    private final String memberName;
    private final String jobRole;
    private final String phoneNum;
    private final Date startAt;
    private final Date closeAt;

    public ProjectDetailsDto(Long id, String projectName, String description, String devOrgName,
                             String profileImageUrl, String memberName, String jobRole,
                             String phoneNum, Date startAt, Date closeAt) {
        this.id = id;
        this.projectName = projectName;
        this.description = description;
        this.devOrgName = devOrgName;
        this.profileImageUrl = profileImageUrl;
        this.memberName = memberName;
        this.jobRole = jobRole;
        this.phoneNum = phoneNum;
        this.startAt = startAt;
        this.closeAt = closeAt;
    }

}
