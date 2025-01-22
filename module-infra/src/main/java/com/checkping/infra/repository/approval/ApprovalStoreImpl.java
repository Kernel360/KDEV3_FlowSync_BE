package com.checkping.infra.repository.approval;

import com.checkping.domain.approval.Approval;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ApprovalStoreImpl implements ApprovalStore {

    private final ApprovalRepository approvalRepository;

    public ApprovalStoreImpl(ApprovalRepository approvalRepository) {
        this.approvalRepository = approvalRepository;
    }

    @Override
    public Approval store(Approval approval) {
        return approvalRepository.save(approval);
    }
}
