package com.checkping.service.approval;

import com.checkping.domain.approval.Approval;
import com.checkping.dto.approval.ApprovalRegister;
import com.checkping.infra.repository.approval.ApprovalStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalStore approvalStore;

    @Override
    public ApprovalRegister.Response register(Long projectId, ApprovalRegister.Request request) {

        // TODO : registerId 는 시큐리티에서 가져오도록 변경 필요
        Long registerId = 123123L;

        Approval init = ApprovalRegister.Request.toEntity(projectId, request.getProgressStepId(),
            registerId, request);

        Approval approval = approvalStore.store(init);

        return ApprovalRegister.Response.toDto(approval);
    }
}
