package com.checkping.infra.repository.project.querydsl;

import com.checkping.domain.project.projection.ProjectCountByManagementStep;

import java.util.List;

public interface ProjectRepositoryCustom {
    List<ProjectCountByManagementStep> countProjectsByManagementStep(Long orgId, Long memberId);
}
