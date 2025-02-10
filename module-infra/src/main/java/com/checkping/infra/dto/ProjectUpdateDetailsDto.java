package com.checkping.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
public class ProjectUpdateDetailsDto {
    private final Long id;
    private final String name;
    private final String description;
    private final String detail;
    private final String managementStep;
    private final Date startAt;
    private final Date closeAt;
    private final Long devOwnerId;
    private final Long developerOrgId;
    private final Long customerOrgId;
}
