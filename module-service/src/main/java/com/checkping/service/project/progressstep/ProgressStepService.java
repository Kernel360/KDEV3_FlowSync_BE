package com.checkping.service.project.progressstep;

import com.checkping.dto.project.ProgressStepDelete;
import com.checkping.dto.project.ProgressStepGet;
import com.checkping.dto.project.ProgressStepOrderUpdater;
import com.checkping.dto.project.ProgressStepPlanUpdate;
import com.checkping.dto.project.ProgressStepRegister;
import com.checkping.dto.project.ProgressStepUpdater;
import java.util.List;

public interface ProgressStepService {
    List<ProgressStepGet.Response> getProgressStep(Long projectId);

    ProgressStepPlanUpdate.Response updateProgressStepPlan(Long projectId, Long progressStepId, ProgressStepPlanUpdate.Request request);

    ProgressStepRegister.Response registerProgressStep(Long projectId, ProgressStepRegister.Request request);

    List<ProgressStepOrderUpdater.Response> updateProgressStepOrder(Long projectId, ProgressStepOrderUpdater.Request request);

    ProgressStepDelete.Response deleteProgressStep(Long projectId, Long progressStepId);

    ProgressStepUpdater.Response update(Long projectId, Long progressStepId, ProgressStepUpdater.Request request);

    ProgressStepGet.Response getInfo(Long projectId, Long progressStepId);
}
