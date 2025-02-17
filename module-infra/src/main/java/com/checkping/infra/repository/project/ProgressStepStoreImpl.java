package com.checkping.infra.repository.project;

import com.checkping.domain.project.ProgressStep;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgressStepStoreImpl implements ProgressStepStore {

    private final ProgressStepRepository progressStepRepository;

    @Override
    public ProgressStep store(ProgressStep progressStep) {
        return progressStepRepository.save(progressStep);
    }
}
