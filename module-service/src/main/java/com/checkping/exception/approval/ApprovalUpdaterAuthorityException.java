package com.checkping.exception.approval;

import com.checkping.common.enums.ErrorCode;

public class ApprovalUpdaterAuthorityException extends ApprovalException {

    public ApprovalUpdaterAuthorityException() {
        super("결재 수정, 삭제는 결재 작성자나 개발사 최고 담당자에게 허용된 요청입니다.", ErrorCode.BAD_REQUEST);
    }
}
