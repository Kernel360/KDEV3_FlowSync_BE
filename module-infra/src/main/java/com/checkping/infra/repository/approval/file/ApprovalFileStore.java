package com.checkping.infra.repository.approval.file;

import com.checkping.domain.approval.ApprovalFile;

public interface ApprovalFileStore {

    ApprovalFile store(ApprovalFile approvalFile);
}
