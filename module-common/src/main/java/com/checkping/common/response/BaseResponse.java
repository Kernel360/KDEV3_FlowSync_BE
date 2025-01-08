package com.checkping.common.response;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.enums.SuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

@Getter
@ToString
@Builder
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {

    private int code;
    private Result result; // enum
    private String message;
    private T data;

    // 성공 응답 (메시지와 데이터 포함)
    public static <T> BaseResponse<T> success(T data, String message) {
        return BaseResponse.<T>builder()
                .code(SuccessCode.OK.getStatusCode())
                .result(Result.SUCCESS)
                .message(message)
                .data(data)
                .build();
    }

    // 성공 응답 (메시지만 포함)
    public static <T> BaseResponse<T> success(String message) {
        return BaseResponse.<T>builder()
                .code(SuccessCode.OK.getStatusCode())
                .result(Result.SUCCESS)
                .message(message)
                .build();
    }

    // 성공 응답 (데이터만 포함)
    public static <T> BaseResponse<T> success(T data) {
        return success(data, null);
    }

    // 실패 응답 (ErrorCode로 생성)
    public static BaseResponse fail(ErrorCode errorCode) {
        return BaseResponse.builder()
                .code(errorCode.getStatusCode())
                .result(Result.FAIL)
                .message(errorCode.getMessage())
                .build();
    }

    // 실패 응답 (기본 에러 코드 사용)
    public static BaseResponse fail() {
        return fail(ErrorCode.BAD_REQUEST);
    }

    // 응답 결과 타입
    public enum Result {
        SUCCESS, FAIL
    }
}

