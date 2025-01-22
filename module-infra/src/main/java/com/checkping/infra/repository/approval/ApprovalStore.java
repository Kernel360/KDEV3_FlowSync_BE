package com.checkping.infra.repository.approval;

import com.checkping.domain.approval.Approval;

public interface ApprovalStore {

    Approval store(Approval approval);
}
