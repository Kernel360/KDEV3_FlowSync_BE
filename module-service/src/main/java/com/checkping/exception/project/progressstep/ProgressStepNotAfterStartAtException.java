package com.checkping.exception.project.progressstep;

import com.checkping.common.enums.ErrorCode;

public class ProgressStepNotAfterStartAtException extends ProgressStepException {

    public ProgressStepNotAfterStartAtException() {
        super("프로젝트 진행단계가 프로젝트에 속하지 않습니다.", ErrorCode.INVALID_INPUT_VALUE);
    }
}
