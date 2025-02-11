package com.checkping.infra.repository.project;

import com.checkping.domain.project.ProgressStep;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressStepRepository extends JpaRepository<ProgressStep, Long> {

    List<ProgressStep> findByProjectId(Long projectId);

    Optional<ProgressStep> findByIdAndProjectId(Long progressStepId, Long projectId);
}
