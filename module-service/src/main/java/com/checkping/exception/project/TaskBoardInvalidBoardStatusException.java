package com.checkping.exception.project;

import com.checkping.common.enums.ErrorCode;

public class TaskBoardInvalidBoardStatusException extends TaskBoardException {

    public TaskBoardInvalidBoardStatusException(String value) {
        super("허용되는 게시글 상태가 아닙니다. BoardCategory : {}" + value, ErrorCode.BAD_REQUEST);
    }
}
