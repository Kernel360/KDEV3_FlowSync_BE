package com.checkping.exception.member;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;

public class OrganizationException extends BaseException {

    public OrganizationException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
