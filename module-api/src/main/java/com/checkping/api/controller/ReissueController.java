package com.checkping.api.controller;

import com.checkping.api.auth.util.CookieUtil;
import com.checkping.common.response.BaseResponse;
import com.checkping.service.member.auth.ReissueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ReissueController {

    private final ReissueService reissueService;

    public ReissueController(ReissueService reissueService) {
        this.reissueService = reissueService;
    }

    @GetMapping("/reissue")
    public BaseResponse<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // Get refresh token
        String refresh = reissueService.validateAndExtractRefreshToken(request.getCookies());
        reissueService.checkTokenValidity(refresh);

        // Extract email and role
        String name = reissueService.getNameFromToken(refresh);
        String email = reissueService.getEmailFromToken(refresh);
        String role = reissueService.getRoleFromToken(refresh);
        Long id = reissueService.getIdFromToken(refresh);

        // Generate new tokens
        String newAccess = reissueService.generateAccessToken(name,id, email, role);
        String newRefresh = reissueService.generateRefreshToken(name, id,email, role);

        // Set response
        response.addCookie(CookieUtil.createCookie("access", newAccess));
        response.addCookie(CookieUtil.createCookie("refresh", newRefresh));

        return BaseResponse.success("토큰이 재발급 되었습니다.");
    }
}