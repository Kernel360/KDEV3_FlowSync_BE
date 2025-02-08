package com.checkping.api.exceptionhandler;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.common.exception.CustomException;
import com.checkping.common.response.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseResponse> handlerCustomException(CustomException e, HttpServletRequest request) {
        logRequestDetails(request, MDC.get("requestId"));
        log.error("Response [{}] msg={}", MDC.get("requestId"), e.getMessage(), e);

        return ResponseEntity
                .status(e.getErrorCode().getStatusCode())
                .body(BaseResponse.fail(e.getErrorCode()));
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse> handlerBaseException(BaseException e, HttpServletRequest request) {
        logRequestDetails(request, MDC.get("requestId"));
        log.error("Response [{}] msg={}", MDC.get("requestId"), e.getMessage(), e);

        return ResponseEntity
                .status(e.getErrorCode().getStatusCode())
                .body(BaseResponse.fail(e.getMessage(), e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handlerValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        logRequestDetails(request, MDC.get("requestId"));
        log.error("Response [{}] msg={}", MDC.get("requestId"), e.getMessage(), e);

        List<String> errorMessages = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.getStatus())
                .body(BaseResponse.fail(errorMessages.toString(), ErrorCode.BAD_REQUEST));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handlerException(Exception e, HttpServletRequest request) {
        logRequestDetails(request, MDC.get("requestId"));
        log.error("Response [{}] msg={}", MDC.get("requestId"), e.getMessage(), e);

        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(BaseResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    private void logRequestDetails(HttpServletRequest request, String requestId) {
        StringBuilder msg = new StringBuilder();
        msg.append("Incoming Request [").append(requestId).append("] ");
        msg.append("method=").append(request.getMethod()).append(", ");
        msg.append("uri=").append(request.getRequestURI()).append(", ");
        String queryString = request.getQueryString();
        if (queryString != null) {
            msg.append("query=").append(queryString).append(", ");
        }
        msg.append("clientIP=").append(request.getRemoteAddr()).append(", ");

        // 헤더 로깅
        msg.append("headers={");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            msg.append(headerName).append("=").append(headerValue).append(", ");
        }
        msg.append("}");

        // 쿠키 로깅
        if (request.getCookies() != null) {
            msg.append(", cookies=[");
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                msg.append(cookie.getName()).append("=").append(cookie.getValue()).append(", ");
            }
            msg.append("]");
        }

        // 요청 본문 로깅 (ContentCachingRequestWrapper 사용)
        String payload = "";
        if (request instanceof ContentCachingRequestWrapper wrappedRequest) {
            byte[] buf = wrappedRequest.getContentAsByteArray();
            if (buf.length > 0) {
                payload = new String(buf, StandardCharsets.UTF_8);
            }
        }

        if (!payload.isEmpty()) {
            msg.append(", body=").append(payload);
        }

        log.error(msg.toString());
    }
}
