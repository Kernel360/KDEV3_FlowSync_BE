package com.checkping.exception.approval;

import com.checkping.common.enums.ErrorCode;

public class ApprovalMismatchProjectException extends ApprovalException {

    public ApprovalMismatchProjectException() {
        super("해당 프로젝트에 속한 결재 글이 아닙니다.", ErrorCode.BAD_REQUEST);
    }
}
