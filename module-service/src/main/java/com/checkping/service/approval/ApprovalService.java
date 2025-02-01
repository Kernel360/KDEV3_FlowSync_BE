package com.checkping.service.approval;

import com.checkping.dto.approval.ApprovalRegister;
import com.checkping.dto.approval.ApprovalSearch;
import com.checkping.dto.approval.ApprovalSearchCondition;
import com.checkping.dto.approval.comment.ApprovalCommentRegister;

public interface ApprovalService {

    ApprovalRegister.Response register(Long projectId, ApprovalRegister.Request request);

    ApprovalSearch.Response search(Long projectId, ApprovalSearchCondition request);

    ApprovalCommentRegister.Response registerComment(Long projectId, Long approvalId, ApprovalCommentRegister.Request request);
}
