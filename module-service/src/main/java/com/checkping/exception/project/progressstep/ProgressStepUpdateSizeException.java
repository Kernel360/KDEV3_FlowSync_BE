package com.checkping.exception.project.progressstep;

import com.checkping.common.enums.ErrorCode;

public class ProgressStepUpdateSizeException extends ProgressStepException {

    public ProgressStepUpdateSizeException() {
        super("변경하려는 프로젝트 진행 단계의 개수와 저장된 프로젝트 진행 단계의 개수가 일치하지 않습니다.", ErrorCode.INVALID_INPUT_VALUE);
    }
}
