package com.checkping.infra.repository.approval;

public interface ApprovalCustomRepository {

    Long countByProgressStep(Long projectId, Long progressStepId);
}
