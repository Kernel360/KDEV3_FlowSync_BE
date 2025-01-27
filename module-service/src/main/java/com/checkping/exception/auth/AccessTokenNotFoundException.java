package com.checkping.exception.auth;

import com.checkping.common.enums.ErrorCode;

public class AccessTokenNotFoundException extends TokenException {

    public AccessTokenNotFoundException() {
        super("Access Token을 찾을 수 없습니다.", ErrorCode.ACCESS_TOKEN_NOT_FOUND);
    }
}
