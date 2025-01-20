package com.checkping.exception.question;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuestionException extends BaseException {

    public QuestionException(ErrorCode errorCode) {
        super(errorCode);
    }

    public QuestionException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
