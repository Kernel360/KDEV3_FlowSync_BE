package com.checkping.exception.approval;

import com.checkping.common.enums.ErrorCode;

public class ApprovalNotFoundEntityException extends ApprovalException {

    public ApprovalNotFoundEntityException() {
        super("해당하는 결재를 찾을 수 없습니다.", ErrorCode.BAD_REQUEST);
    }
}
