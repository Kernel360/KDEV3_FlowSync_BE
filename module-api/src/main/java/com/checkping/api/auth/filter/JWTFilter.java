package com.checkping.api.auth.filter;


import com.checkping.api.auth.util.ResponseUtil;
import com.checkping.common.enums.ErrorCode;
import com.checkping.common.response.BaseResponse;
import com.checkping.exception.auth.AccessTokenNotFoundException;
import com.checkping.service.member.util.JwtUtil;
import com.checkping.service.member.auth.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JWTFilter(JwtUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    // TODO 필터 거치지 않을 경로 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // h2-console 경로는 필터 제외
        if (request.getRequestURI().startsWith("/h2-console")) {
            return true;
        }

        // login 경로는 필터 제외
        if (request.getRequestURI().startsWith("/login")) {
            return true;
        }

        // 리프레시 토큰 재발급 시 필터 제외
        if (request.getRequestURI().startsWith(("/reissue"))) {
            return true;
        }

        // 회원 생성 시 필터 제외
        if (request.getRequestURI().equals("/admins/members")){
            return true;
        }

        // 업체 생성시 필터 제외
        if (request.getRequestURI().equals("/admins/organizations")){
            return true;
        }

//        // 비밀번호 까먹었을 때 재설정 요청 시 필터 제외


        //return super.shouldNotFilter(request);
            return false;
    }


    /*
    TODO
     #1. 권한이 필요한 요청인데 토큰이 없는 경우 - Spring Security가 기본적으로 처리
     #2. 권한이 필요 없는 경우에도 만료된 토큰이 들어오면 접근 차단하는 문제 해결*/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 쿠키에서 토큰 추출
        Cookie[] cookies = request.getCookies();

        // 쿠키가 없는 경우
        if (cookies == null) {

            BaseResponse<Void> errorResponse = BaseResponse.fail(ErrorCode.UNAUTHORIZED);
            ResponseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, errorResponse);
            return;
        }

        // access 토큰 추출
        Cookie cookie = null;

        for (Cookie c : cookies) {
            if ("access".equals(c.getName())) {
                cookie = c;
                break;
            }
        }

        String accessToken = cookie.getValue();


        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            String errorMessage = "access token expired";
            ResponseUtil.sendErrorResponse(response, HttpStatus.valueOf(HttpServletResponse.SC_UNAUTHORIZED), errorMessage);
            return;
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {
            String errorMessage = "invalid access token";
            ResponseUtil.sendErrorResponse(response, HttpStatus.valueOf(HttpServletResponse.SC_UNAUTHORIZED), errorMessage);
            return;
        }

        // username, role 값을 획득
        String name = jwtUtil.getName(accessToken);
        String email = jwtUtil.getEmail(accessToken);
        String role = jwtUtil.getRole(accessToken);
        String password = "PASSWORDFORTOKEN";

        CustomUserDetails customUserDetails = new CustomUserDetails(name, email, role, password);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
}
