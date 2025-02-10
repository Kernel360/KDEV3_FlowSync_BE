package com.checkping.exception.project;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;

public class ProjectException extends BaseException {

    public ProjectException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ProjectException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
