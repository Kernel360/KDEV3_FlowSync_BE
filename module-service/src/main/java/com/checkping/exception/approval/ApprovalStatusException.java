package com.checkping.exception.approval;

import com.checkping.common.enums.ErrorCode;

public class ApprovalStatusException extends ApprovalException {

    public ApprovalStatusException(String value) {
        super("허용되는 결재 상태가 아닙니다. Category : {}" + value, ErrorCode.BAD_REQUEST);
    }
}
