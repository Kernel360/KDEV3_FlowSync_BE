package com.checkping.service.member.auth;

import com.checkping.exception.auth.*;
import com.checkping.infra.repository.member.MemberRepository;
import com.checkping.service.member.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
public class ReissueService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final TokenBlacklistService tokenBlacklistService; // 추가

    public ReissueService(JwtUtil jwtUtil, MemberRepository memberRepository, TokenBlacklistService tokenBlacklistService) {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
        this.tokenBlacklistService = tokenBlacklistService; // 추가
    }

    /**
     * 쿠키에서 Refresh Token 추출 및 검증
     */
    public String validateAndExtractRefreshToken(Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            throw new RefreshTokenNotFoundException();
        }

        for (Cookie cookie : cookies) {
            if ("refresh".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new RefreshTokenNotFoundException();
    }

    /**
     * Refresh Token 블랙리스트 확인
     */
    public boolean isRefreshTokenBlacklisted(String refreshToken) {
        return tokenBlacklistService.isRefreshTokenBlacklisted(refreshToken); // 수정
    }

    /**
     * Refresh Token 유효성 검증
     */
    public void checkTokenValidity(String refreshToken) {
        try {
            // 🔹 블랙리스트에 있는지 확인
            if (tokenBlacklistService.isRefreshTokenBlacklisted(refreshToken)) { // 수정
                throw new BlacklistedTokenException();
            }

            // 토큰이 만료되었는지 확인
            if (jwtUtil.isExpired(refreshToken)) {
                throw new RefreshTokenExpiredException();
            }

            // 토큰의 카테고리 확인
            String category = jwtUtil.getCategory(refreshToken);
            if (!"refresh".equals(category)) {
                throw new RefreshTokenCategoryException();
            }

            // 토큰에서 이메일을 추출하고, 해당 사용자가 존재하는지 확인
            final String email = jwtUtil.getEmail(refreshToken);
            if (!memberRepository.existsByEmail(email)) {
                throw new RefreshTokenNotFoundException();
            }

        } catch (BlacklistedTokenException e) {
            throw new InvalidTokenException(); // 블랙리스트에 있으면 InvalidTokenException으로 변환
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenExpiredException();
        } catch (RefreshTokenCategoryException | RefreshTokenNotFoundException e) {
            throw e; // 이미 위에서 처리된 예외는 그대로 던짐
        } catch (Exception e) {
            throw new InvalidTokenException(); // 그 외 예상하지 못한 예외 처리
        }
    }

    /**
     * 토큰에서 사용자 이름 추출
     */
    public String getNameFromToken(String token) {
        return jwtUtil.getName(token);
    }

    /**
     * 토큰에서 이메일 추출
     */
    public String getEmailFromToken(String token) {
        return jwtUtil.getEmail(token);
    }

    /**
     * 토큰에서 역할 추출
     */
    public String getRoleFromToken(String token) {
        return jwtUtil.getRole(token);
    }

    public Long getIdFromToken(String token) {
        return jwtUtil.getMemberId(token); // JwtUtil에서 호출
    }

    /**
     * 새로운 Access Token 생성
     */
    public String generateAccessToken(String name, Long id,  String email, String role) {
        return jwtUtil.createJwt("access",id, name, email, role, 1440);
    }

    /**
     * 새로운 Refresh Token 생성
     */
    public String generateRefreshToken(String name, Long id, String email, String role) {
        return jwtUtil.createJwt("refresh", id, name, email, role, 1440);
    }
}