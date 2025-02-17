package com.checkping.service.project.progressstep;

import com.checkping.dto.project.ProgressStepGet;
import com.checkping.dto.project.ProgressStepPlanUpdate;
import com.checkping.dto.project.ProgressStepRegister;
import java.util.List;

public interface ProgressStepService {
    List<ProgressStepGet.Response> getProgressStep(Long projectId);

    ProgressStepPlanUpdate.Response updateProgressStepPlan(Long projectId, Long progressStepId, ProgressStepPlanUpdate.Request request);

    ProgressStepRegister.Response registerProgressStep(Long projectId, ProgressStepRegister.Request request);
}
