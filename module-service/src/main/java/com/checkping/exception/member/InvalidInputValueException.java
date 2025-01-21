package com.checkping.exception.member;

import com.checkping.common.enums.ErrorCode;

public class InvalidInputValueException extends MemberException{

    public InvalidInputValueException() {
        super("페이지 번호와 사이즈는 1보다 커야합니다.", ErrorCode.INVALID_INPUT_VALUE);
    }

    public InvalidInputValueException(String message) {
        super(message, ErrorCode.INVALID_INPUT_VALUE);
    }
}
