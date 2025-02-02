package com.checkping.service.member.auth;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.member.response.MemberResponseDto;
import com.checkping.exception.auth.InvalidTokenException;
import com.checkping.exception.auth.LoginFailureException;
import com.checkping.exception.auth.LogoutFailureException;
import com.checkping.exception.auth.RefreshTokenNotFoundException;
import com.checkping.service.member.util.CurrentMemberUtil;
import com.checkping.service.member.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CurrentMemberUtil currentMemberUtil;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public BaseResponse getCurrentMember() {
        return BaseResponse.success(MemberResponseDto.MeResponseDto.fromEntity(currentMemberUtil.getCurrentMember()));
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
            //TODO 엑세스 토큰 유효시간 개발 기간동안 120분으로 연장, 추후 15분으로 변경
            String accessToken = jwtUtil.createJwt("access", name, email, role, 120);
            String refreshToken = jwtUtil.createJwt("refresh", name, email, role, 1440);

            // 4) 토큰을 반환
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
            throw new LogoutFailureException();
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

        // TODO:  refresh 토큰을 블랙리스트 처리
    }
}