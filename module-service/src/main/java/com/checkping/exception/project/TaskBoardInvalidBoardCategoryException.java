package com.checkping.exception.project;

import com.checkping.common.enums.ErrorCode;

public class TaskBoardInvalidBoardCategoryException extends TaskBoardException {

    public TaskBoardInvalidBoardCategoryException(String value) {
        super("허용되는 게시글 유형이 아닙니다. BoardCategory : {}" + value, ErrorCode.BAD_REQUEST);
    }
}
