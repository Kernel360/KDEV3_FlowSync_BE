package com.checkping.service.member.auth;

import com.checkping.exception.auth.InvalidTokenException;
import com.checkping.exception.auth.LoginFailureException;
import com.checkping.exception.auth.RefreshTokenNotFoundException;
import com.checkping.service.member.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 로그인 처리
     * - 인증 실패 시 예외 던지기
     * - 인증 성공 시 access/refresh 토큰 생성 → 쿠키 설정(쿠키 세팅은 Controller에서 처리해도 됨)
     */
    public AuthTokens login(String email, String password) {
        try {
            // 1) 인증 시도
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, password);
            Authentication authResult = authenticationManager.authenticate(authToken);

            // 2) 인증 성공 시 사용자 정보 추출
            CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
            String role = authResult.getAuthorities().iterator().next().getAuthority();
            String name = userDetails.getName();

            // 3) JWT 생성
            String accessToken = jwtUtil.createJwt("access", name, email, role, 15);
            String refreshToken = jwtUtil.createJwt("refresh", name, email, role, 1440);

            // 4) 토큰 묶음을 반환 (Controller에서 쿠키로 만들어 응답할 수도 있음)
            return new AuthTokens(accessToken, refreshToken);

        } catch (Exception e) {
            // 인증 실패 시 예외 던짐
            System.out.println(e);
            throw new LoginFailureException();
        }
    }

    /**
     * 로그아웃 처리
     * - refresh 쿠키가 없거나 유효하지 않으면 예외 던지기
     */
    public void logout(HttpServletRequest request) {
        // 1) 쿠키에서 refresh 추출
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new RefreshTokenNotFoundException();
        }

        String refresh = null;
        for (Cookie cookie : cookies) {
            if ("refresh".equals(cookie.getName())) {
                refresh = cookie.getValue();
                break;
            }
        }

        if (refresh == null) {
            throw new RefreshTokenNotFoundException();
        }

        // 2) refresh 토큰인지 확인
        String category = jwtUtil.getCategory(refresh); // 파싱 실패 시 예외 발생 가능
        if (!"refresh".equals(category)) {
            throw new InvalidTokenException();
        }

        // (필요하다면 서버 측에서 refresh 토큰을 블랙리스트 처리 등)

        // 로그아웃 자체는 쿠키 제거(Controller단에서 처리) 등으로 완성
    }
}