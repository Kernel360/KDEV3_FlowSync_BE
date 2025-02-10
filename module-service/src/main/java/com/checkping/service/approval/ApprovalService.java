package com.checkping.service.approval;

import com.checkping.dto.approval.ApprovalConfirm;
import com.checkping.dto.approval.ApprovalDelete;
import com.checkping.dto.approval.ApprovalGet;
import com.checkping.dto.approval.ApprovalRegister;
import com.checkping.dto.approval.ApprovalSearch;
import com.checkping.dto.approval.ApprovalSearchCondition;
import com.checkping.dto.approval.ApprovalUpdate;
import com.checkping.dto.approval.comment.ApprovalCommentRegister;
import com.checkping.dto.approval.comment.ApprovalReCommentRegister;

public interface ApprovalService {

    ApprovalRegister.Response register(Long projectId, ApprovalRegister.Request request);

    ApprovalSearch.Response search(Long projectId, ApprovalSearchCondition request);

    ApprovalGet.Response get(Long projectId, Long approvalId);

    ApprovalUpdate.Response update(Long projectId, Long approvalId, ApprovalUpdate.Request request);

    ApprovalDelete.Response delete(Long projectId, Long approvalId);

    ApprovalCommentRegister.Response registerComment(Long projectId, Long approvalId,
        ApprovalCommentRegister.Request request);

    ApprovalReCommentRegister.Response registerReComment(Long projectId, Long approvalId,
        Long commentId, ApprovalReCommentRegister.Request request);

    ApprovalConfirm.Response confirm(Long projectId, Long approvalId,
        ApprovalConfirm.Request request);
}
