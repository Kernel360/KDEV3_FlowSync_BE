package com.checkping.api.auth.interceptor;

import com.checkping.exception.request.DuplicateRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class DuplicatedClickPrevent implements HandlerInterceptor {

    private final StringRedisTemplate stringRedisTemplate;
    private static final String PREFIX = "CLICK:ID:";

    public DuplicatedClickPrevent(@Qualifier("redisTemplateForDuplicatedClick") StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public boolean checkAndSetRequest(String email) {
        String key = PREFIX + email;
        Boolean success = stringRedisTemplate.opsForValue()
                .setIfAbsent(key, "LOCK", 1, TimeUnit.SECONDS);

        return success != null && success;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // 1. GET 요청이면 바로 통과
        if ("GET".equalsIgnoreCase(method)) {
            return true;
        }

        // 2. 특정 경로 PUT 요청 제외
        if ("PUT".equalsIgnoreCase(method) && requestURI.matches("^/projects/\\d+/progress-steps/orders$")) {
            return true;
        }

        Object memberId = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getAttribute("memberId");

        if (memberId == null) {
            log.warn("member ID가 없습니다. 중복 체크를 패스합니다.");
            return true; // memberId가 없으면 중복 체크 패스
        }

        if(!checkAndSetRequest(memberId.toString())) {
            throw new DuplicateRequestException();
        }

        return true;
    }
}