package com.checkping.api.controller;

import com.checkping.common.response.BaseResponse;
import com.checkping.service.member.auth.ReissueService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ReissueController {

    private final ReissueService reissueService;

    public ReissueController(ReissueService reissueService) {
        this.reissueService = reissueService;
    }

    @PostMapping("/reissue")
    public BaseResponse<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // Get refresh token
        String refresh = reissueService.validateAndExtractRefreshToken(request.getCookies());
        reissueService.checkTokenValidity(refresh);

        // Extract email and role
        String name = reissueService.getNameFromToken(refresh);
        String email = reissueService.getEmailFromToken(refresh);
        String role = reissueService.getRoleFromToken(refresh);

        // Generate new tokens
        String newAccess = reissueService.generateAccessToken(name, email, role);
        String newRefresh = reissueService.generateRefreshToken(name, email, role);

        // Set response
        response.setHeader("Authorization", "Bearer " + newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        return BaseResponse.success("Reissue success");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        return cookie;
    }
}