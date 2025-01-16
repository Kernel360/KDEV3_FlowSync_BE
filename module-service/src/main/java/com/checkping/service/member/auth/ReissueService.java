package com.checkping.service.member.auth;

import com.checkping.exception.auth.RefreshTokenCategoryException;
import com.checkping.exception.auth.RefreshTokenExpiredException;
import com.checkping.exception.auth.RefreshTokenNotFoundException;
import com.checkping.infra.repository.member.MemberRepository;
import com.checkping.service.member.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class ReissueService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public ReissueService(JwtUtil jwtUtil, MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    public String validateAndExtractRefreshToken(Cookie[] cookies) {

        if (cookies == null || cookies.length == 0) {
            throw new RefreshTokenNotFoundException();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                return cookie.getValue();
            }
        }

        throw new RefreshTokenNotFoundException();
    }

    public void checkTokenValidity(String refreshToken) {

        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenExpiredException();
        }

        String category = jwtUtil.getCategory(refreshToken);
        if (!"refresh".equals(category)) {
            throw new RefreshTokenCategoryException();
        }

        final String email = jwtUtil.getEmail(refreshToken);
        if(memberRepository.existsByEmail(email) == false) {
            throw new RefreshTokenNotFoundException();
        }

        //TODO JWT 형식에 맞지 않은 경우 체크
        //TODO 그 외 한 번에 예외처리



    }

    public String getCategoryFromToken(String token) {
        return jwtUtil.getCategory(token);
    }

    public String getNameFromToken(String token) {
        return jwtUtil.getName(token);
    }

    public String getEmailFromToken(String token) {
        return jwtUtil.getEmail(token); // JwtUtil에서 호출
    }

    public String getRoleFromToken(String token) {
        return jwtUtil.getRole(token); // JwtUtil에서 호출
    }

    public String generateAccessToken(String name, String email, String role) {
        return jwtUtil.createJwt("access", name, email, role, 15);
    }

    public String generateRefreshToken(String name, String email, String role) {
        return jwtUtil.createJwt("refresh", name, email, role, 1440);
    }
}