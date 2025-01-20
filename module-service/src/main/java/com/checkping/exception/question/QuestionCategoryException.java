package com.checkping.exception.question;

import com.checkping.common.enums.ErrorCode;

public class QuestionCategoryException extends QuestionException {

    public QuestionCategoryException(String value) {
        super("허용되는 게시글 유형이 아닙니다. BoardCategory : {}" + value, ErrorCode.BAD_REQUEST);
    }
}
