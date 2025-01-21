package com.checkping.exception.question.comment;

import com.checkping.common.enums.ErrorCode;
import com.checkping.exception.question.QuestionException;

public class QuestionCommentNotFoundEntityException extends QuestionException {

    public QuestionCommentNotFoundEntityException() {
        super("업무 관리 게시글 댓글을 찾을 수 없습니다.", ErrorCode.NOT_FOUND);
    }
}
