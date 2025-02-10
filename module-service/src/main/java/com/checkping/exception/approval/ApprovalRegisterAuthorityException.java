package com.checkping.exception.approval;

import com.checkping.common.enums.ErrorCode;

public class ApprovalRegisterAuthorityException extends ApprovalException {

    public ApprovalRegisterAuthorityException() {
        super("해당 요청은 결재의 작성자에게만 허용된 요청입니다.", ErrorCode.BAD_REQUEST);
    }
}
