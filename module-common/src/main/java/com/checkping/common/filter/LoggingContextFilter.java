package com.checkping.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

@Component
public class LoggingContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 요청 ID 생성 및 MDC에 저장
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        request.setAttribute("requestId", requestId); // 컨트롤러에서 필요할 경우 사용 가능

        // 본문이 있는 요청만 ContentCachingRequestWrapper로 감싸기
        HttpServletRequest wrappedRequest = request;
        if (shouldWrapRequest(request)) {
            wrappedRequest = new ContentCachingRequestWrapper(request);
        }

        // 응답을 ContentCachingResponseWrapper로 감싸기
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            MDC.remove("requestId"); // ✅ `finally`에서 즉시 제거
            wrappedResponse.copyBodyToResponse(); // 응답 본문 원래 스트림으로 복사
        }
    }

    /**
     * 본문이 있는 HTTP 메서드인지 확인하여 ContentCachingRequestWrapper 적용 여부를 결정
     */
    private boolean shouldWrapRequest(HttpServletRequest request) {
        String method = request.getMethod();
        return "POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method);
    }
}
