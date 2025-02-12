package com.checkping.exception.approval;

import com.checkping.common.enums.ErrorCode;

public class ApprovalCategoryException extends ApprovalException {

    public ApprovalCategoryException() {
        super("허용되는 결재 유형이 아닙니다.", ErrorCode.BAD_REQUEST);
    }
}
