package com.checkping.exception.request;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;

public class DuplicateRequestException extends BaseException {

    private ErrorCode errorCode;

    public DuplicateRequestException() {
        super(ErrorCode.ALREADY_PROCESSING_REQUEST);
    }

    public DuplicateRequestException(String message) {
        super(message, ErrorCode.ALREADY_PROCESSING_REQUEST);
    }
}
