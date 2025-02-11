package com.checkping.exception.auth;

import com.checkping.common.enums.ErrorCode;

public class BlacklistedTokenException extends TokenException{

    public BlacklistedTokenException() {
        super("블랙리스트에 등록된 토큰입니다.", ErrorCode.BLACKLISTED_TOKEN);
    }
}