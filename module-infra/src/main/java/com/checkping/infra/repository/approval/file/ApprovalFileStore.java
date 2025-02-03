package com.checkping.infra.repository.approval.file;

import com.checkping.domain.approval.ApprovalFile;
import java.util.List;

public interface ApprovalFileStore {

    ApprovalFile store(ApprovalFile approvalFile);

    List<ApprovalFile> store(List<ApprovalFile> approvalFiles);
}
