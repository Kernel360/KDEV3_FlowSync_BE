package com.checkping.infra.repository.project;

import com.checkping.domain.project.ProgressStep;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgressStepReaderImpl implements ProgressStepReader {

    private final ProgressStepRepository progressStepRepository;

    @Override
    public List<ProgressStep> getByProjectId(Long projectId) {
        return progressStepRepository.findByProjectId(projectId);
    }

}
