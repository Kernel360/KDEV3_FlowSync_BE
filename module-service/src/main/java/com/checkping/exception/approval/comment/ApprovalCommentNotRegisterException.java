package com.checkping.exception.approval.comment;

import com.checkping.common.enums.ErrorCode;
import com.checkping.exception.approval.ApprovalException;

public class ApprovalCommentNotRegisterException extends ApprovalException {

    public ApprovalCommentNotRegisterException() {
        super("결재 댓글 작성자가 아닙니다. 요청을 확인해주세요.", ErrorCode.BAD_REQUEST);
    }
}
