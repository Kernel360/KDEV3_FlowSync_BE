package com.checkping.infra.repository.approval;

import com.checkping.domain.approval.ApprovalCompleteHistory;

public interface ApprovalCompleteHistoryStore {

    ApprovalCompleteHistory store(ApprovalCompleteHistory approvalCompleteHistory);
}
