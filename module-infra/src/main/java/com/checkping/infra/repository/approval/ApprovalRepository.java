package com.checkping.infra.repository.approval;

import com.checkping.domain.approval.Approval;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {

    @Query("SELECT a FROM Approval a "
        + "LEFT JOIN FETCH a.commentList c "
        + "WHERE a.id = :approvalId "
        + "ORDER BY COALESCE(c.parent.id, c.id), c.regAt ASC")
    Optional<Approval> getByIdWithComments(@Param("approvalId") Long approvalId);

    Page<Approval> findByProjectId(Long projectId, Pageable pageable);

    Page<Approval> findByProjectIdAndTitleContaining(Long projectId, String title, Pageable pageable);

    Page<Approval> findByProjectIdAndStatus(Long projectId, Approval.ApprovalStatus status, Pageable pageable);

    Page<Approval> findByProjectIdAndProgressStepId(Long projectId, Long progressStepId, Pageable pageable);

    Page<Approval> findByProjectIdAndTitleContainingAndStatus(Long projectId, String title, Approval.ApprovalStatus status, Pageable pageable);

    Page<Approval> findByProjectIdAndTitleContainingAndProgressStepId(Long projectId, String title, Long progressStepId, Pageable pageable);

    Page<Approval> findByProjectIdAndProgressStepIdAndStatus(Long projectId, Long progressStepId, Approval.ApprovalStatus status, Pageable pageable);

    Page<Approval> findByProjectIdAndTitleContainingAndProgressStepIdAndStatus(Long projectId, String title, Long progressStepId, Approval.ApprovalStatus status, Pageable pageable);

    boolean existsByProjectIdAndId(Long projectId, Long approvalId);
}
