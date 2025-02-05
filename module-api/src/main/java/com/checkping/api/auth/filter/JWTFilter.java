package com.checkping.api.auth.filter;


import com.checkping.api.auth.util.ResponseUtil;
import com.checkping.common.enums.ErrorCode;
import com.checkping.common.response.BaseResponse;
import com.checkping.service.member.auth.CustomUserDetails;
import com.checkping.service.member.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JWTFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JWTFilter(JwtUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }
    // TODO 필터 거치지 않을 경로 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();

        // 기존 제외 경로
        if (uri.startsWith("/h2-console") ||
                uri.startsWith("/login") ||
                uri.startsWith("/reissue")) {
            return true;
        }

        // 스웨거 관련 경로 제외
        if (uri.startsWith("/swagger-ui/") ||
                uri.equals("/swagger-ui") ||
                uri.startsWith("/v3/api-docs")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 쿠키에서 토큰 추출
        Cookie[] cookies = request.getCookies();

        // 쿠키가 없는 경우
        if (cookies == null) {

            BaseResponse<Void> errorResponse = BaseResponse.fail(ErrorCode.COOKIES_NOT_FOUND);
            ResponseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, errorResponse);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

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

            BaseResponse<Void> errorResponse = BaseResponse.fail(ErrorCode.EXPIRED_JWT_ACCESS_TOKEN);
            ResponseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, errorResponse);

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
        Long id = jwtUtil.getMemberId(accessToken);
        String password = "PASSWORDFORTOKEN";

        CustomUserDetails customUserDetails = new CustomUserDetails(id, name, email, role, password);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, List.of(new SimpleGrantedAuthority(customUserDetails.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
}
