package com.checkping.api.auth.filter;

import com.checkping.api.auth.util.CookieUtil;
import com.checkping.service.member.auth.CustomUserDetails;
import com.checkping.service.member.auth.RedisConnectionCheckService;
import com.checkping.service.member.auth.TokenBlacklistService;
import com.checkping.service.member.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;
    private final StringRedisTemplate redisTemplateForInactiveMembers;
    private final RedisConnectionCheckService redisConnectionCheckService;

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

        // 토큰 유무 확인, 토큰이 없으면 401 응답
        if (accessToken == null) {
            throw new AuthenticationException("Access Token이 없습니다.");
        }

        // 토큰 만료 및 유효성 여부 확인, 만료되었거나 만료 검증 중 유효하지 않은 토큰인 경우 401 응답
        if(jwtUtil.isExpired(accessToken)){
            throw new AuthenticationException("Access Token이 만료되었거나 유효하지 않습니다.");
        }

        Long id = jwtUtil.getMemberId(accessToken);

        if (redisConnectionCheckService.isRedisAvailable()) {

            // 요청한 Access Token이 블랙리스트에 등록된 토큰인지 확인
            if (tokenBlacklistService.isAccessTokenBlacklisted(accessToken)) {
                throw new AuthenticationException("블랙리스트에 등록된 토큰입니다. 요청된 토큰 발급회원 id: " + id);
            }

            // 요청한 access token의 id가 비활성화되었거나 삭제된 회원인지 확인
            Boolean isInactive = redisTemplateForInactiveMembers.hasKey("inactive:member:" + id);
            Boolean isDeleted = redisTemplateForInactiveMembers.hasKey("deleted:member:" + id);
            if (Boolean.TRUE.equals(isInactive) || Boolean.TRUE.equals(isDeleted)) {
                // 비활성화된 회원이 보낸 토큰을 블랙리스트에 추가
                tokenBlacklistService.blacklistAccessToken(accessToken, jwtUtil.getExpiration(accessToken));
                // 리프레시 토큰도 블랙리스트에 추가
                String refreshToken = jwtUtil.extractToken(request, "refresh");
                tokenBlacklistService.blacklistRefreshToken(refreshToken, jwtUtil.getExpiration(refreshToken));
                // 요청한 사용자의 모든 토큰을 쿠키에서 삭제
                Cookie delAccess = CookieUtil.deleteCookie("access");
                Cookie delRefresh = CookieUtil.deleteCookie("refresh");
                response.addCookie(delAccess);
                response.addCookie(delRefresh);

                throw isInactive ? new AuthenticationException("비활성화된 회원입니다. 요청된 토큰 발급회원 id: " + id) : new AuthenticationException("삭제된 회원입니다. 요청된 토큰 발급회원 id: " + id);
            }
        }

        String name = jwtUtil.getName(accessToken);
        String email = jwtUtil.getEmail(accessToken);
        request.setAttribute("email", email);
        String role = jwtUtil.getRole(accessToken);

        CustomUserDetails customUserDetails = new CustomUserDetails(id, name, email, role, "PASSWORDFORTOKEN");
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, List.of(new SimpleGrantedAuthority(customUserDetails.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
