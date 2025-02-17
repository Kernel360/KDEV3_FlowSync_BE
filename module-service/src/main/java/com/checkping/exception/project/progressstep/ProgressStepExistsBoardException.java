package com.checkping.exception.project.progressstep;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;

public class ProgressStepExistsBoardException extends BaseException {

    public ProgressStepExistsBoardException() {
        super("프로젝트 진행단계에 속하는 결재 글이나 질문 글이 존재합니다.", ErrorCode.INVALID_INPUT_VALUE);
    }
}
