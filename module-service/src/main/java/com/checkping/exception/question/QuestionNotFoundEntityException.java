package com.checkping.exception.question;

import com.checkping.common.enums.ErrorCode;

public class QuestionNotFoundEntityException extends QuestionException {

    public QuestionNotFoundEntityException() {
        super("업무 관리 게시글을 찾을 수 없습니다.", ErrorCode.NOT_FOUND);
    }
}
