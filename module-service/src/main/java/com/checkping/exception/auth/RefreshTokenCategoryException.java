package com.checkping.exception.auth;

import com.checkping.common.enums.ErrorCode;

public class RefreshTokenCategoryException extends TokenException {

    public RefreshTokenCategoryException() {
        super("Refresh Token의 카테고리가 일치하지 않습니다.", ErrorCode.INVALID_JWT_REFRESH_TOKEN);
    }
}
