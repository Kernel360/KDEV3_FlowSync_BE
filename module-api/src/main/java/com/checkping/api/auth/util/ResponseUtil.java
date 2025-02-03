package com.checkping.api.auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class ResponseUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 실패 응답 생성
    public static void sendErrorResponse(HttpServletResponse response, HttpStatus status, Object errorResponse) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status.value());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    // 성공 응답 생성
    public static void sendSuccessResponse(HttpServletResponse response, HttpStatus status, Object successResponse) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status.value());
        response.getWriter().write(objectMapper.writeValueAsString(successResponse));
    }

    // JWT와 쿠키 설정 포함 응답
    public static void sendSuccessWithJwtAndCookie(HttpServletResponse response, HttpStatus status, Object successResponse, String accessToken, String refreshToken) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status.value());
        response.addCookie(CookieUtil.createCookie("access", accessToken));
        response.addCookie(CookieUtil.createCookie("refresh", refreshToken));
        response.getWriter().write(objectMapper.writeValueAsString(successResponse));
    }
}
