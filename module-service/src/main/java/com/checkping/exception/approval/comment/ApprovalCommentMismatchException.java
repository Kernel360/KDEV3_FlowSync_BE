package com.checkping.exception.approval.comment;

import com.checkping.common.enums.ErrorCode;
import com.checkping.exception.approval.ApprovalException;

public class ApprovalCommentMismatchException extends ApprovalException {

    public ApprovalCommentMismatchException() {
        super("해당 결재 글에 속한 댓글이 아닙니다.", ErrorCode.BAD_REQUEST);
    }
}
