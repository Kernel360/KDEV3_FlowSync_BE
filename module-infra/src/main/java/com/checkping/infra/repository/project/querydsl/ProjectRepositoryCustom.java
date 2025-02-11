package com.checkping.infra.repository.project.querydsl;

import com.checkping.domain.project.projection.OwnerInfo;
import com.checkping.domain.project.projection.ProjectCountByManagementStep;
import com.checkping.domain.project.projection.ProjectInfo;
import com.checkping.domain.project.projection.ProjectListInfoByManagementStep;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProjectRepositoryCustom {
    List<ProjectCountByManagementStep> countProjectsByManagementStep(Long orgId, Long memberId);

    Page<ProjectListInfoByManagementStep> findProjectsByManagementSteps(Long orgId, Long memberId, String manageStep, Pageable pageable);

    List<Long> memberByProject(Long memberId, Pageable pageable);

    Optional<ProjectInfo> findProjectInfoById(Long id);

    Optional<OwnerInfo> findOwnerMemberInfoById(Long memberId);
}
