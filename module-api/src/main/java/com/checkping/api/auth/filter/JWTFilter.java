package com.checkping.api.auth.filter;


import com.checkping.api.auth.util.ResponseUtil;
import com.checkping.service.member.auth.CustomUserDetails;
import com.checkping.service.member.auth.TokenBlacklistService;
import com.checkping.service.member.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    // TODO 필터 거치지 않을 경로 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();

        return uri.startsWith("/h2-console") ||
                uri.startsWith("/login") ||
                uri.startsWith("/reissue") ||
                uri.startsWith("/swagger-ui/") ||
                uri.equals("/swagger-ui") ||
                uri.startsWith("/v3/api-docs");
    }

    /**
     * JWT 검증 및 인증 처리
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Access Token 쿠키에서 추출
        String accessToken = JwtUtil.extractToken(request, "access");

        // 토큰이 없으면 401 응답
        if (accessToken == null) {
            ResponseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Access token required");
            return;
        }

        try {
            // 토큰 만료 여부 확인
            jwtUtil.isExpired(accessToken);

            // 블랙리스트 확인 (prefix 적용)
            if (tokenBlacklistService.isAccessTokenBlacklisted(accessToken)) {
                ResponseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "블랙리스트에 등록된 엑세스 토큰입니다");
                return;
            }

            // 토큰이 access인지 확인
            if (!"access".equals(jwtUtil.getCategory(accessToken))) {
                ResponseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Invalid access token");
                return;
            }

            // 사용자 정보 추출
            String name = jwtUtil.getName(accessToken);
            String email = jwtUtil.getEmail(accessToken);
            String role = jwtUtil.getRole(accessToken);
            Long id = jwtUtil.getMemberId(accessToken);


            CustomUserDetails customUserDetails = new CustomUserDetails(id, name, email, role, "PASSWORDFORTOKEN");
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, List.of(new SimpleGrantedAuthority(customUserDetails.getRole())));
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (ExpiredJwtException e) {
            ResponseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Access token expired");
            return;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ResponseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
