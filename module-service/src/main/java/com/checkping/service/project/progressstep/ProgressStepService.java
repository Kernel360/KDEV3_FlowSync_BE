package com.checkping.service.project.progressstep;

import com.checkping.dto.project.ProgressStepGet;
import java.util.List;

public interface ProgressStepService {
    List<ProgressStepGet.Response> getProgressStep(Long projectId);
}
