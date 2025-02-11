package com.checkping.exception.project.progressstep;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;

public class ProgressStepNotFoundException extends BaseException {

    public ProgressStepNotFoundException() {
        super("프로젝트 진행단계를 찾을 수 없거나 프로젝트에 속하지 않은 진행단계 입니다.", ErrorCode.NOT_FOUND);
    }
}
