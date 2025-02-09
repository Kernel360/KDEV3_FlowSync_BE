package com.checkping.service.member.util;

import com.checkping.service.member.auth.TokenBlacklistService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final TokenBlacklistService tokenBlacklistService;

    public JwtUtil(@Value("${spring.jwt.secret}") String secret, TokenBlacklistService tokenBlacklistService) {
        byte[] byteSecretKey = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(byteSecretKey);
        this.tokenBlacklistService = tokenBlacklistService;
    }

    /**
     * JWT에서 카테고리(category) 가져오기
     */
    public String getCategory(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("category", String.class);
    }

    /**
     * JWT에서 이메일(email) 가져오기
     */
    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }

    /**
     * JWT에서 역할(role) 가져오기
     */
    public String getRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    /**
     * JWT에서 사용자 이름(name) 가져오기
     */
    public String getName(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("name", String.class);
    }

    /**
     * 토큰이 만료되었는지 확인
     */
    public boolean isExpired(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            return true; // 토큰 파싱에 실패하면 만료된 것으로 간주
        }
    }

    /**
     * Access Token 블랙리스트 확인
     */
    public boolean isAccessTokenBlacklisted(String token) {
        return tokenBlacklistService.isAccessTokenBlacklisted("access_" + token);
    }

    /**
     * Refresh Token 블랙리스트 확인
     */
    public boolean isRefreshTokenBlacklisted(String token) {
        return tokenBlacklistService.isRefreshTokenBlacklisted("refresh_" + token);
    }

    /**
     * 쿠키에서 특정 토큰을 추출
     */
    public static String extractToken(HttpServletRequest request, String tokenName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (tokenName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * JWT 만료 시간 가져오기
     */
    public long getExpiration(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }

    public Long getMemberId(String token) {

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("memberId", Long.class);
    }

    /**
     * JWT 생성 (Access/Refresh 토큰)
     */
    public String createJwt(String category, Long memberId, String name, String email, String role, int expiredMin) {
        return Jwts.builder()
                .claim("category", category)
                .claim("memberId", memberId)
                .claim("name", name)
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expiredMin).toInstant()))
                .signWith(key)
                .compact();
    }
}