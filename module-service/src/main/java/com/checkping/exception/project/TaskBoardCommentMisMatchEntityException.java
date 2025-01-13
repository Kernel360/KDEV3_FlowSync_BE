package com.checkping.exception.project;

import com.checkping.common.enums.ErrorCode;

public class TaskBoardCommentMisMatchEntityException extends TaskBoardException {

    public TaskBoardCommentMisMatchEntityException() {
        super("업무 관리 게시글에 속하지 않은 댓글입니다.", ErrorCode.BAD_REQUEST);
    }
}
