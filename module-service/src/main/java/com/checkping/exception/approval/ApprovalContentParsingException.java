package com.checkping.exception.approval;

import com.checkping.common.enums.ErrorCode;

public class ApprovalContentParsingException extends ApprovalException {

    public ApprovalContentParsingException() {
        super("결재 글 파싱에 실패했습니다.", ErrorCode.INVALID_INPUT_VALUE);
    }
}
