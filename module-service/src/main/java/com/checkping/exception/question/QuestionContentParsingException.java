package com.checkping.exception.question;

import com.checkping.common.enums.ErrorCode;

public class QuestionContentParsingException extends QuestionException {

    public QuestionContentParsingException() {
        super("게시글 파싱에 실패하였습니다.", ErrorCode.INVALID_INPUT_VALUE);
    }
}
