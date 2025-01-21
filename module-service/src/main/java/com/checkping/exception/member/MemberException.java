package com.checkping.exception.member;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;

public class MemberException extends BaseException {

    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MemberException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
