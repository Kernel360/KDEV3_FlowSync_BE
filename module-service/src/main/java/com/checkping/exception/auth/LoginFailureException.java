package com.checkping.exception.auth;

import com.checkping.common.enums.ErrorCode;

public class LoginFailureException extends LoginException {

    public LoginFailureException() {
        super("로그인에 실패하였습니다.", ErrorCode.LOGIN_FAILED);
    }

}
