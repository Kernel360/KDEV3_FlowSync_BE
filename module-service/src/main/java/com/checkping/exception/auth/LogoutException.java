package com.checkping.exception.auth;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;

public class LogoutException extends BaseException {

    public LogoutException(ErrorCode errorCode) {
        super(errorCode);
    }

    public LogoutException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}