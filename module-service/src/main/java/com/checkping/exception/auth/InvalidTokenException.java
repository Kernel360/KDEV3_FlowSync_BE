package com.checkping.exception.auth;

import com.checkping.common.enums.ErrorCode;

public class InvalidTokenException extends TokenException {

    public InvalidTokenException() {
        super("유효하지 않은 토큰입니다.", ErrorCode.INVALID_JWT_REFRESH_TOKEN);
    }
}
