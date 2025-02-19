package com.checkping.exception.project.progressstep;

import com.checkping.common.enums.ErrorCode;

public class ProgressStepNotDevOwnerException extends ProgressStepException {

    public ProgressStepNotDevOwnerException() {
        super("프로젝트 개발사 최고 담당자에게 허용된 동작입니다. 권한을 확인해주세요.", ErrorCode.INSUFFICIENT_PERMISSIONS);
    }
}
