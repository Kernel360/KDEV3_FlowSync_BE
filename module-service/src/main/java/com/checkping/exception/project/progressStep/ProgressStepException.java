package com.checkping.exception.project.progressStep;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;

public class ProgressStepException extends BaseException {

    public ProgressStepException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ProgressStepException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
