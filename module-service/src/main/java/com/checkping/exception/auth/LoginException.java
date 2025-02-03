package com.checkping.exception.auth;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;

public class LoginException extends BaseException {

    public LoginException(ErrorCode errorCode) {
        super(errorCode);
    }

    public LoginException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}