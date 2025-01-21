package com.checkping.exception.question.comment;

import com.checkping.common.enums.ErrorCode;
import com.checkping.exception.question.QuestionException;

public class QuestionCommentMisMatchEntityException extends QuestionException {

    public QuestionCommentMisMatchEntityException() {
        super("업무 관리 게시글에 속하지 않은 댓글입니다.", ErrorCode.BAD_REQUEST);
    }
}
