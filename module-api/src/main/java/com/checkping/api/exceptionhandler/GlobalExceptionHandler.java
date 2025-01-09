package com.checkping.api.exceptionhandler;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.common.exception.CustomException;
import com.checkping.common.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public BaseResponse handlerCustomException(CustomException e) {
        return BaseResponse.fail(e.getErrorCode());
    }

    @ExceptionHandler(BaseException.class)
    public BaseResponse handlerBaseException(BaseException e) {
        log.error("BaseException occurred: errorCode={}, message={}", e.getErrorCode(), e.getMessage(), e);
        return BaseResponse.fail(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse handlerException(Exception e) {
        log.error("Unhandled exception: message={}", e.getMessage(), e);
        return BaseResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
