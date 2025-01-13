package com.checkping.api.security.handler;

import com.checkping.api.security.util.JWTUtil;
import com.checkping.service.member.security.MemberDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("---------------------------LoginSuccessHandler Start--------------------------------");

        log.info(authentication.toString());
        log.info("-----------------------------------------------------------");

        MemberDto memberDto = (MemberDto) authentication.getPrincipal();

        Map<String, Object> claims = memberDto.getClaims();

        String accessToken = JWTUtil.generateToken(claims,1);
        String refreshToken = JWTUtil.generateToken(claims,60*24);

        // AccessToken을 헤더에 추가
        response.setHeader("Authorization", "Bearer " + accessToken);

        // RefreshToken을 쿠키에 추가
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true); // JavaScript로 접근하지 못하도록 설정
        refreshTokenCookie.setSecure(true); // HTTPS 통신에서만 전송되도록 설정 (배포 환경에서 권장)
        refreshTokenCookie.setPath("/"); // 쿠키를 사용할 경로 설정
        refreshTokenCookie.setMaxAge(60 * 60 * 24); // 24시간 동안 유효

        response.addCookie(refreshTokenCookie);

//        Gson gson = new Gson();
//
//        String jsonStr = gson.toJson(claims);
//
//        response.setContentType("application/json; charset=UTF-8");
//        PrintWriter printWriter = response.getWriter();
//        printWriter.print(jsonStr);
//        printWriter.close();
        log.info("---------------------------LoginSuccessHandler End--------------------------------");
    }
}
