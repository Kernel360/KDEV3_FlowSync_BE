package com.checkping.api.controller.member;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.member.request.LoginRequestDto;
import com.checkping.service.member.auth.AuthService;
import com.checkping.service.member.auth.AuthTokens;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthController implements AuthApi{

    private final AuthService authService;

    /**
     * 로그인
     * - 성공 시 쿠키에 access, refresh 저장 → BaseResponse.success(...)
     * - 실패 시 예외 발생 → GlobalExceptionHandler에서 처리
     */
    @PostMapping("/login")
    public BaseResponse<String> login(@RequestBody LoginRequestDto request, HttpServletResponse response) {
        // 1) Service 호출 (실패 시 예외 발생)
        AuthTokens tokens = authService.login(request.getEmail(), request.getPassword());

        // 2) 성공 시 쿠키 생성
        Cookie accessCookie = new Cookie("access", tokens.getAccess());
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie("refresh", tokens.getRefresh());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        response.addCookie(refreshCookie);

        // 3) 응답
        return BaseResponse.success("로그인에 성공하였습니다.");
    }

    /**
     * 로그아웃
     * - 성공 시 쿠키 제거 → BaseResponse.success(...)
     * - 실패 시 예외 발생 → GlobalExceptionHandler에서 처리
     */
    @PostMapping("/logout")
    public BaseResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // 1) Service 호출 (실패 시 예외 발생)
        authService.logout(request);

        // 2) 쿠키 제거
        Cookie delAccess = new Cookie("access", null);
        delAccess.setMaxAge(0);
        delAccess.setPath("/");
        response.addCookie(delAccess);

        Cookie delRefresh = new Cookie("refresh", null);
        delRefresh.setMaxAge(0);
        delRefresh.setPath("/");
        response.addCookie(delRefresh);

        // 3) 응답
        return BaseResponse.success("로그아웃에 성공하였습니다.");
    }
}