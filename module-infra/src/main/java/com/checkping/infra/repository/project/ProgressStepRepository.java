package com.checkping.infra.repository.project;

import com.checkping.domain.project.ProgressStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressStepRepository extends JpaRepository<ProgressStep, Long> {
}
