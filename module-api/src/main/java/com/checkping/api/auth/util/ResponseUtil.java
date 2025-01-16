package com.checkping.api.auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class ResponseUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

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
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(createCookie("refresh", refreshToken));
        response.getWriter().write(objectMapper.writeValueAsString(successResponse));
    }
}
