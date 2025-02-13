package com.checkping.infra.repository.approval;

import com.checkping.domain.approval.ApprovalCompleteHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalCompleteHistoryRepository extends
    JpaRepository<ApprovalCompleteHistory, Long> {
}
