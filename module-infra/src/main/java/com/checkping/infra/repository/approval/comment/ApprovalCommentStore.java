package com.checkping.infra.repository.approval.comment;

import com.checkping.domain.approval.ApprovalComment;

public interface ApprovalCommentStore {

    ApprovalComment store(ApprovalComment approvalComment);
}
