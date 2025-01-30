package com.checkping.infra.repository.approval;

import com.checkping.domain.approval.Approval;
import com.checkping.info.approval.ApprovalSearchInfo;
import org.springframework.data.domain.Page;

public interface ApprovalReader {

    Page<Approval> getApprovals(Long projectId, ApprovalSearchInfo.SearchCondition searchCondition);
}
