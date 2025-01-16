package com.checkping.exception.auth;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenException extends BaseException{


    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
