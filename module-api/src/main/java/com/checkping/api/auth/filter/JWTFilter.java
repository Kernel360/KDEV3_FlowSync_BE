package com.checkping.api.auth.filter;

import com.checkping.api.auth.util.CookieUtil;
import com.checkping.api.auth.util.ResponseUtil;
import com.checkping.common.enums.ErrorCode;
import com.checkping.common.response.BaseResponse;
import com.checkping.service.member.auth.CustomUserDetails;
import com.checkping.service.member.auth.RedisConnectionCheckService;
import com.checkping.service.member.auth.TokenBlacklistService;
import com.checkping.service.member.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;
    private final StringRedisTemplate redisTemplateForInactiveMembers;
    private final RedisConnectionCheckService redisConnectionCheckService;

    // TODO 필터 거치지 않을 경로 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();

        return uri.startsWith("/h2-console") ||
                uri.startsWith("/login") ||
                uri.startsWith("/reissue") ||
                uri.startsWith("/check")||
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
            BaseResponse errorResponse = BaseResponse.fail(ErrorCode.ACCESS_TOKEN_NOT_FOUND);
            ResponseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, errorResponse);
            return;
        }

        // 엑세스 토큰에서 사용자 ID 추출
        Long id = jwtUtil.getMemberId(accessToken);

        try {
            // 토큰 만료 여부 확인
            jwtUtil.isExpired(accessToken);

            // 3) Redis 블랙리스트, 회원 활성화 여부 검증 (연결 여부 먼저 확인)
            if (redisConnectionCheckService.isRedisAvailable()) {

                // 1. 요청한 토큰 블랙리스트에 있는지 확인
                if (tokenBlacklistService.isAccessTokenBlacklisted(accessToken)) {
                    BaseResponse errorResponse = BaseResponse.fail(ErrorCode.BLACKLISTED_TOKEN);
                    ResponseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, errorResponse);
                    return;
                }

                // 2. 비활성화 회원인지 확인
                Boolean isInactive = redisTemplateForInactiveMembers.hasKey("inactive:member:" + id);

                if (Boolean.TRUE.equals(isInactive)) {
                    // 비활성화된 회원이 보낸 토큰을 블랙리스트에 추가
                    tokenBlacklistService.blacklistAccessToken(accessToken, jwtUtil.getExpiration(accessToken));
                    // 리프레시 토큰도 블랙리스트에 추가
                    String refreshToken = jwtUtil.extractToken(request, "refresh");
                    tokenBlacklistService.blacklistRefreshToken(refreshToken, jwtUtil.getExpiration(refreshToken));

                    // 쿠키 삭제
                    Cookie delAccess = CookieUtil.deleteCookie("access");
                    Cookie delRefresh = CookieUtil.deleteCookie("refresh");
                    response.addCookie(delAccess);
                    response.addCookie(delRefresh);

                    BaseResponse errorResponse = BaseResponse.fail(ErrorCode.INACTIVE_MEMBER);
                    log.info("[JWTFilter] 비활성화된 회원의 요청. id: {}", id);
                    ResponseUtil.sendErrorResponse(response, HttpStatus.FORBIDDEN, errorResponse);
                    return;
                }
            }

            // 사용자 정보 추출
            String name = jwtUtil.getName(accessToken);
            String email = jwtUtil.getEmail(accessToken);
            String role = jwtUtil.getRole(accessToken);

            CustomUserDetails customUserDetails = new CustomUserDetails(id, name, email, role, "PASSWORDFORTOKEN");
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, List.of(new SimpleGrantedAuthority(customUserDetails.getRole())));
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (ExpiredJwtException e) {
            log.info("[JWTFilter] Access Token이 만료되었습니다. id: {}", id);
            BaseResponse<Void> errorResponse = BaseResponse.fail(ErrorCode.EXPIRED_JWT_ACCESS_TOKEN);
            ResponseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, errorResponse);
            return;
        } catch (Exception e) {
            log.info("[JWTFilter] Access Token 검증 중 에러 발생. {}", e);
            log.info("[JWTFilter] 유효하지 않은 Access Token입니다. id: {}", id);
            BaseResponse<Void> errorResponse = BaseResponse.fail(ErrorCode.UNAUTHORIZED);
            ResponseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, errorResponse);
            return;
        }

        filterChain.doFilter(request, response);
    }
}