package com.checkping.infra.repository.approval.comment;

import com.checkping.domain.approval.ApprovalComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalCommentRepository extends JpaRepository<ApprovalComment, Long> {
}
