package com.checkping.service.member.auth;

import com.checkping.exception.auth.InvalidTokenException;
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
            // 토큰이 만료되었는지 확인
            jwtUtil.isExpired(refreshToken);

            // 토큰의 카테고리가 "refresh"인지 확인
            String category = jwtUtil.getCategory(refreshToken);
            if (!"refresh".equals(category)) {
                throw new RefreshTokenCategoryException();
            }

            // 토큰에서 이메일을 추출하고, 해당 사용자가 존재하는지 확인
            final String email = jwtUtil.getEmail(refreshToken);
            if (!memberRepository.existsByEmail(email)) {
                throw new RefreshTokenNotFoundException();
            }

        } catch (ExpiredJwtException e) {
            throw new RefreshTokenExpiredException();
        } catch (RefreshTokenCategoryException | RefreshTokenNotFoundException e) {
            // 이미 위에 처리된 예외는 그대로 던짐
            throw e;
        } catch (Exception e) {
            // 그 외에 예상하지 못한 예외를 InvalidTokenException으로 처리
            throw new InvalidTokenException();
        }
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