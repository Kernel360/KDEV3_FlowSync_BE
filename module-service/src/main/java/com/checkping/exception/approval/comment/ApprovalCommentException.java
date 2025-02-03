package com.checkping.exception.approval.comment;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.exception.approval.ApprovalException;
import lombok.Getter;

@Getter
public class ApprovalCommentException extends ApprovalException {

    public ApprovalCommentException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ApprovalCommentException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
