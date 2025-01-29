package com.checkping.exception.approval;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import lombok.Getter;

@Getter
public class ApprovalException extends BaseException {

    public ApprovalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ApprovalException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
