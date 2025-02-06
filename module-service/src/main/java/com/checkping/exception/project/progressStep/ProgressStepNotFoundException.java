package com.checkping.exception.project.progressStep;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;

public class ProgressStepNotFoundException extends BaseException {

    public ProgressStepNotFoundException() {
        super("프로젝트 진행단계를 찾을 수 없습니다.", ErrorCode.NOT_FOUND);
    }
}
