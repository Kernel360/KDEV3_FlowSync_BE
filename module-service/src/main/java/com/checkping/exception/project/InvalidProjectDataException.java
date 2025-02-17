package com.checkping.exception.project;

import com.checkping.common.enums.ErrorCode;

public class InvalidProjectDataException extends ProjectException {

    public InvalidProjectDataException(String message) {
        super(message, ErrorCode.INVALID_INPUT_VALUE);
    }
}
