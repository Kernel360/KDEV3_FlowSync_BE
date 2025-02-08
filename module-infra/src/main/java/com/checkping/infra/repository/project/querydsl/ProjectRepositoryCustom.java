package com.checkping.infra.repository.project.querydsl;

import com.checkping.domain.project.projection.ProjectCountByManagementStep;
import com.checkping.domain.project.projection.ProjectListInfoByManagementStep;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectRepositoryCustom {
    List<ProjectCountByManagementStep> countProjectsByManagementStep(Long orgId, Long memberId);

    Page<ProjectListInfoByManagementStep> findProjectsByManagementSteps(Long orgId, Long memberId, String manageStep, Pageable pageable);

    List<Long> memberByProject(Long memberId, Pageable pageable);
}
