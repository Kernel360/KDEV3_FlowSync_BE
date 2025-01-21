package com.checkping.exception.auth;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;

public class InactiveMemberException extends BaseException {

    public InactiveMemberException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InactiveMemberException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}