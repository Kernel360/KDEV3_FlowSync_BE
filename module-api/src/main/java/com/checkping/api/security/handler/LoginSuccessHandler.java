package com.checkping.api.security.handler;

import com.checkping.api.security.util.JWTUtil;
import com.checkping.service.member.security.MemberDto;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
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

        claims.put("accessToken", accessToken);
        claims.put("refreshToken", refreshToken);

        Gson gson = new Gson();

        String jsonStr = gson.toJson(claims);

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jsonStr);
        printWriter.close();
        log.info("---------------------------LoginSuccessHandler End--------------------------------");
    }
}
