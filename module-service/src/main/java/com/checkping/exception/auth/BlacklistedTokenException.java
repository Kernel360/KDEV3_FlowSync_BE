package com.checkping.exception.auth;

import com.checkping.common.enums.ErrorCode;

public class BlacklistedTokenException extends TokenException{

    public BlacklistedTokenException() {
        super("Blacklisted Token", ErrorCode.BLACKLISTED_TOKEN);
    }
}