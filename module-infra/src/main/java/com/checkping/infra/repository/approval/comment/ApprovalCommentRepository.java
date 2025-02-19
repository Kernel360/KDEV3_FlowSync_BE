package com.checkping.infra.repository.approval.comment;

import com.checkping.domain.approval.ApprovalComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalCommentRepository extends JpaRepository<ApprovalComment, Long> {

    @Query("SELECT COUNT(ac) > 0 FROM ApprovalComment ac "
        + "WHERE ac.approval.id = :approvalId AND ac.id = :id")
    boolean existsByApprovalIdAndId(@Param("approvalId") Long approvalId, @Param("id") Long id);
}
