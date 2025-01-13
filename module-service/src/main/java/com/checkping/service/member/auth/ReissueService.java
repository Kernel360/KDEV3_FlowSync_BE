package com.checkping.service.member.auth;

import com.checkping.service.member.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class ReissueService {

    private final JwtUtil jwtUtil;

    public ReissueService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String validateAndExtractRefreshToken(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public void checkTokenValidity(String refreshToken) {
        if (refreshToken == null) {
            throw new IllegalArgumentException("Refresh token is null");
        }

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("Refresh token expired");
        }

        String category = jwtUtil.getCategory(refreshToken);
        if (!"refresh".equals(category)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
    }

    public String getEmailFromToken(String token) {
        return jwtUtil.getEmail(token); // JwtUtil에서 호출
    }

    public String getRoleFromToken(String token) {
        return jwtUtil.getRole(token); // JwtUtil에서 호출
    }

    public String generateAccessToken(String email, String role) {
        return jwtUtil.createJwt("access", email, role, 600000L);
    }

    public String generateRefreshToken(String email, String role) {
        return jwtUtil.createJwt("refresh", email, role, 86400000L);
    }
}