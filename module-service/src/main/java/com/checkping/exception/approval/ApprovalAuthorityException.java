package com.checkping.exception.approval;

import com.checkping.common.enums.ErrorCode;

public class ApprovalAuthorityException extends ApprovalException {

    public ApprovalAuthorityException() {
        super("해당 행동을 할 수 있는 권한이 부여되지 않았습니다.", ErrorCode.BAD_REQUEST);
    }
}
