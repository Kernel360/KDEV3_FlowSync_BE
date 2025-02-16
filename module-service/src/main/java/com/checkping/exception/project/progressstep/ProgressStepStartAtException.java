package com.checkping.exception.project.progressstep;

import com.checkping.common.enums.ErrorCode;

public class ProgressStepStartAtException extends ProgressStepException {

    public ProgressStepStartAtException() {
        super("프로젝트 진행단계의 시작기간을 설정하는 과정에서 이슈가 발생하였습니다.", ErrorCode.NOT_FOUND);
    }
}
