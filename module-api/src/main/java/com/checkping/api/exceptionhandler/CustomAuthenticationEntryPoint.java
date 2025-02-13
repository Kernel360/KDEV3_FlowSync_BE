package com.checkping.api.exceptionhandler;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.response.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 인증 실패 시 401 에러 처리하는 클래스
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        log.error("Authentication Entry Point : {}", authException.getMessage());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        String message = "인증이 필요합니다.";

        // JWT 만료 예외가 발생한 경우 메시지를 변경
        Throwable cause = authException.getCause();
        if (cause instanceof ExpiredJwtException) {
            errorCode = ErrorCode.EXPIRED_JWT_ACCESS_TOKEN; // 새로운 에러 코드 추가 가능
            message = "토큰이 만료되었습니다. 다시 로그인하세요.";
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        BaseResponse<?> errorResponse = BaseResponse.fail(message, errorCode);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}