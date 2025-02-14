package com.checkping.infra.repository.approval;

import com.checkping.domain.approval.ApprovalCompleteHistory;
import com.checkping.info.approval.ApprovalCompleteHistorySearchInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApprovalCompleteHistoryCustomRepository {

    Page<ApprovalCompleteHistory> search(Long projectId, ApprovalCompleteHistorySearchInfo condition, Pageable pageable);
}
