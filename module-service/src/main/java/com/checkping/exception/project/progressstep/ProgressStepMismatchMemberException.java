package com.checkping.exception.project.progressstep;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;

public class ProgressStepMismatchMemberException extends BaseException {

    public ProgressStepMismatchMemberException() {
        super("회원이 프로젝트에 속하지 않습니다.", ErrorCode.INVALID_INPUT_VALUE);
    }
}
