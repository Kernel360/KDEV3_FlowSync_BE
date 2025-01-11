package com.checkping.exception.project;

import com.checkping.common.enums.ErrorCode;

public class TaskBoardCommentNotFoundEntityException extends TaskBoardException {

    public TaskBoardCommentNotFoundEntityException() {
        super("업무 관리 게시글 댓글을 찾을 수 없습니다.", ErrorCode.NOT_FOUND);
    }
}
