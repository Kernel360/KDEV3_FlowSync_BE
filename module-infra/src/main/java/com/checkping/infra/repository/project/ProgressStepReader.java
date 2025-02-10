package com.checkping.infra.repository.project;

import com.checkping.domain.project.ProgressStep;
import java.util.List;
import java.util.Optional;

public interface ProgressStepReader {

    List<ProgressStep> getByProjectId(Long projectId);

    Optional<ProgressStep> getById(Long progressStepId);

    Optional<ProgressStep> getByIdAndProjectId(Long progressStepId, Long projectId);
}
