package com.checkping.exception.question;

import com.checkping.common.enums.ErrorCode;

public class QuestionStatusException extends QuestionException {

    public QuestionStatusException(String value) {
        super("허용되는 게시글 상태가 아닙니다. BoardCategory : {}" + value, ErrorCode.BAD_REQUEST);
    }
}
