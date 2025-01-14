package com.checkping.api.security.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class LoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        log.info("---------------------------LoginFailHandler Start--------------------------------");

        Gson gson = new Gson();

        String jsonStr = gson.toJson(Map.of("result","fail","message","ERROR_LOGIN"));

        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jsonStr);
        printWriter.close();

        log.info("---------------------------LoginFailHandler End--------------------------------");
    }
}