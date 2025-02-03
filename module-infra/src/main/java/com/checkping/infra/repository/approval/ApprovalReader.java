package com.checkping.infra.repository.approval;

import com.checkping.domain.approval.Approval;
import com.checkping.info.approval.ApprovalSearchInfo;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ApprovalReader {

    Optional<Approval> getById(Long approvalId);

    Optional<Approval> getByIdWithComments(Long approvalId);

    Page<Approval> getApprovals(Long projectId, ApprovalSearchInfo.SearchCondition searchCondition);

    boolean isContainingApproval(Long projectId, Long approvalId);
}
