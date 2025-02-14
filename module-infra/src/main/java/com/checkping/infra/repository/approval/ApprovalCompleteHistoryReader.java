package com.checkping.infra.repository.approval;

import com.checkping.domain.approval.ApprovalCompleteHistory;
import com.checkping.info.approval.ApprovalCompleteHistorySearchInfo;
import org.springframework.data.domain.Page;

public interface ApprovalCompleteHistoryReader {

    Page<ApprovalCompleteHistory> search(Long projectId, ApprovalCompleteHistorySearchInfo condition);
}
