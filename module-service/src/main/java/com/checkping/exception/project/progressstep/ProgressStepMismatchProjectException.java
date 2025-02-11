package com.checkping.exception.project.progressstep;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;

public class ProgressStepMismatchProjectException extends BaseException {

    public ProgressStepMismatchProjectException() {
        super("프로젝트 진행단계가 프로젝트에 속하지 않습니다.", ErrorCode.INVALID_INPUT_VALUE);
    }
}
