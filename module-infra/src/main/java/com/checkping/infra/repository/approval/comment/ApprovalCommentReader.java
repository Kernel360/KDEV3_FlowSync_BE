package com.checkping.infra.repository.approval.comment;

import com.checkping.domain.approval.ApprovalComment;
import java.util.Optional;

public interface ApprovalCommentReader {

    Optional<ApprovalComment> getById(Long id);

    boolean isContainingComment(Long approvalId, Long commentId);
}
