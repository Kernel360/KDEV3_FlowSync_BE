package com.checkping.api.controller;

import com.checkping.api.security.util.CustomJWTException;
import com.checkping.api.security.util.JWTUtil;
import com.checkping.common.response.BaseResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class APIRefreshController {

    @RequestMapping("/admins/members/refresh")
    public BaseResponse<String> refresh(
            @RequestHeader("Authorization") String authHeader,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws CustomJWTException {

        log.info("---------------------------RefreshToken Start--------------------------------");

        String refreshToken = getRefreshTokenFromCookie(request);

        if(refreshToken == null) {
            throw new CustomJWTException("NULL_REFRESH");
        }

        if(authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID_STRING");
        }

        String accessToken = authHeader.substring(7);

        //Access 토큰이 만료되지 않았다면
        if(checkExpiredToken(accessToken) == false) {
            log.info("---------------------------RefreshToken End (access 만료 x)--------------------------------");
            log.info("---------------------------RefreshToken End--------------------------------");

            // 헤더에 기존 accessToken 추가
            response.setHeader("Authorization", "Bearer " + accessToken);

            // 쿠키에 기존 refreshToken 추가
            addRefreshTokenToCookie(response, refreshToken);

            return BaseResponse.success("발급 성공");
        }

        //Refresh 토큰 검증
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);

        log.info("refresh .... claims: " + claims);

        String newAccessToken = JWTUtil.generateToken(claims, 1);
        String newRefreshToken = checkTime((Integer)claims.get("exp")) == true ?
                JWTUtil.generateToken(claims,60*24) : refreshToken;

        log.info("---------------------------RefreshToken End (access 만료)--------------------------------");
        log.info("---------------------------RefreshToken End--------------------------------");

        // 헤더에 새 accessToken 추가
        response.setHeader("Authorization", "Bearer " + newAccessToken);

        // 쿠키에 새 refreshToken 추가
        addRefreshTokenToCookie(response, newRefreshToken);

        return BaseResponse.success("발급 성공");
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    // 쿠키에 refreshToken 추가
    private void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30); // 30일 동안 유효
        response.addCookie(cookie);
    }

    // 시간이 1시간 미만으로 남았다면
    private boolean checkTime(Integer exp) {
        // JWT exp를 날짜로 변환
        Date expDate = new Date((long) exp * (1000));

        // 현재 시간과의 차이 계산 - 밀리세컨즈
        long gap = expDate.getTime() - System.currentTimeMillis();

        // 분단위 계산
        long leftMin = gap / (1000 * 60);

        // 1시간도 안남았는지...
        return leftMin < 60;
    }

    private boolean checkExpiredToken(String token) {
        try {
            JWTUtil.validateToken(token);
        } catch (CustomJWTException ex) {
            if(ex.getMessage().equals("Expired")) {
                return true;
            }
        }
        return false;
    }
}