package com.checkping.exception.question;

import com.checkping.common.enums.ErrorCode;

public class QuestionMisMatchEntityException extends QuestionException {

    public QuestionMisMatchEntityException() {
        super("프로젝트에 속하지 않은 질문입니다.", ErrorCode.BAD_REQUEST);
    }
}
