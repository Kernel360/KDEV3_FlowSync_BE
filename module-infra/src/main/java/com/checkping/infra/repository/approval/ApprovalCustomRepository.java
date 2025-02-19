package com.checkping.infra.repository.approval;

import com.checkping.domain.approval.Approval;
import com.checkping.info.approval.ApprovalCountProjection;
import com.checkping.info.approval.ApprovalSearchInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApprovalCustomRepository {

    List<ApprovalCountProjection> countByProgressStep(Long projectId, boolean isAdmin);

    Page<Approval> getByCondition(Long projectId, ApprovalSearchInfo.SearchCondition searchCondition,
        Pageable pageable);

    Optional<Approval> getApprovalWithComment(Long projectId, Long approvalId, boolean isDeleted);
}
