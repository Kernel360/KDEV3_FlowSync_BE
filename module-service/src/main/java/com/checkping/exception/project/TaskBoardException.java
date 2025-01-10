package com.checkping.exception.project;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskBoardException extends BaseException {

    public TaskBoardException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TaskBoardException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
