package com.checkping.infra.repository.project;

import com.checkping.domain.project.ProgressStep;

public interface ProgressStepStore {

    ProgressStep store(ProgressStep progressStep);

    void delete(ProgressStep progressStep);
}
