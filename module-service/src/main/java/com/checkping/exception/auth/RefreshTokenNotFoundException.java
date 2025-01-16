package com.checkping.exception.auth;

import com.checkping.common.enums.ErrorCode;

public class RefreshTokenNotFoundException extends TokenException {

    public RefreshTokenNotFoundException() {
        super("Refresh Token을 찾을 수 없습니다.", ErrorCode.INVALID_JWT_REFRESH_TOKEN);
    }
}
