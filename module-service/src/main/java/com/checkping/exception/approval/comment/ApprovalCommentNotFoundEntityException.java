package com.checkping.exception.approval.comment;

import com.checkping.common.enums.ErrorCode;
import com.checkping.exception.approval.ApprovalException;

public class ApprovalCommentNotFoundEntityException extends ApprovalException {

    public ApprovalCommentNotFoundEntityException() {
        super("해당 댓글을 찾을 수 없습니다.", ErrorCode.BAD_REQUEST);
    }
}
