package com.checkping.exception.auth;

import com.checkping.common.enums.ErrorCode;

public class LogoutFailureException extends LogoutException {

    public LogoutFailureException() {
        super("로그인된 사용자가 아닙니다.\n 로그아웃에 실패하였습니다.", ErrorCode.LOGOUT_FAILURE);
    }
}