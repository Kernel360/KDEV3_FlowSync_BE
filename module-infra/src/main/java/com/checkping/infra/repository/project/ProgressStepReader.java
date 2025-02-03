package com.checkping.infra.repository.project;

import com.checkping.domain.project.ProgressStep;
import java.util.List;

public interface ProgressStepReader {

    List<ProgressStep> getByProjectId(Long projectId);
}
