package com.checkping.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ProjectDetailsDto {
    private final Long id;
    private final String projectName;
    private final String description;
    private final String devOrgName;
    private final String profileImageUrl;
    private final String memberName;
    private final String jobRole;
    private final String jobTitle;
    private final String phoneNum;
    private final Date startAt;
    private final Date closeAt;
}
