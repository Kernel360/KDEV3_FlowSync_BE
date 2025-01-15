package com.checkping.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /*
        400 Bad Request
    */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 형식 또는 누락된 데이터가 있습니다."),
    UNKNOWN_USER(HttpStatus.BAD_REQUEST, "알 수 없는 사용자입니다."),

    /*
        401 Unauthorized
    */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다. Access Token을 확인하세요."),
    INVALID_JWT_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Access Token입니다."),
    EXPIRED_JWT_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token이 만료되었습니다. 재발급 받으세요."),
    INVALID_JWT_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다."),
    EXPIRED_JWT_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 만료되었습니다. 다시 로그인하세요."),

    /*
        403 Forbidden
    */
    FORBIDDEN(HttpStatus.FORBIDDEN, "요청 권한이 부족합니다."),

    /*
        404 Not Found
    */
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

    /*
        409 Conflict
    */
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "이미 존재하는 항목입니다."),
    ALREADY_APPLIED(HttpStatus.CONFLICT, "이미 적용된 상태입니다."),
    INSUFFICIENT_PERMISSIONS(HttpStatus.CONFLICT, "권한이 부족합니다."),

    /*
        500 Internal Server Error
    */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");

    private final HttpStatus status;
    private final String message;

    public int getStatusCode() {
        return status.value();
    }
}
