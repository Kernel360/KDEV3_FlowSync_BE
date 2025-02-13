package com.checkping.infra.repository.approval;

import com.checkping.domain.approval.ApprovalCompleteHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApprovalCompleteHistoryStoreImpl implements ApprovalCompleteHistoryStore {

    private final ApprovalCompleteHistoryRepository approvalCompleteHistoryRepository;

    @Override
    public ApprovalCompleteHistory store(ApprovalCompleteHistory approvalCompleteHistory) {
        return approvalCompleteHistoryRepository.save(approvalCompleteHistory);
    }

}
