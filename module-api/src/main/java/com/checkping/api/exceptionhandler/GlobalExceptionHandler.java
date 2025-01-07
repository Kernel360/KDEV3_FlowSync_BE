package com.checkping.api.exceptionhandler;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.CustomException;
import com.checkping.common.response.BaseResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public BaseResponse handlerCustomException(CustomException e) {
        return BaseResponse.fail(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse handlerException(Exception e) {
        return BaseResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
