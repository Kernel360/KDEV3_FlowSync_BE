package com.checkping.exception.auth;

import com.checkping.common.enums.ErrorCode;

public class RefreshTokenExpiredException extends TokenException{

    public RefreshTokenExpiredException() {
        super("Refresh Token이 만료되었습니다.", ErrorCode.EXPIRED_JWT_REFRESH_TOKEN);
    }
}
