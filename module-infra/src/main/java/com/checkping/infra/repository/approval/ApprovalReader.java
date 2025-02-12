package com.checkping.infra.repository.approval;

import com.checkping.domain.approval.Approval;
import com.checkping.info.approval.ApprovalCountProjection;
import com.checkping.info.approval.ApprovalSearchInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ApprovalReader {

    Optional<Approval> getById(Long approvalId);

    Optional<Approval> getByIdWithComments(Long projectId, Long approvalId);

    Page<Approval> getApprovals(Long projectId, ApprovalSearchInfo.SearchCondition searchCondition);

    boolean existsByProjectIdAndId(Long projectId, Long approvalId);

    List<ApprovalCountProjection> countByProgressStep(Long projectId);

    boolean isApprovalRegister(Long approvalId, Long registerId);
}
