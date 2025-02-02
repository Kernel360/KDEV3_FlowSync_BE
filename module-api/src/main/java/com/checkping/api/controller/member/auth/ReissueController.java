package com.checkping.api.controller.member.auth;

import com.checkping.api.auth.util.CookieUtil;
import com.checkping.common.response.BaseResponse;
import com.checkping.service.member.auth.AuthTokens;
import com.checkping.service.member.auth.ReissueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ReissueController implements ReissueApi {

    private final ReissueService reissueService;

    public ReissueController(ReissueService reissueService) {
        this.reissueService = reissueService;
    }

    @Override
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
        response.addCookie(CookieUtil.createCookie("access", newAccess));
        response.addCookie(CookieUtil.createCookie("refresh", newRefresh));

        AuthTokens tokens = new AuthTokens(newAccess, newRefresh);

        return BaseResponse.success(tokens, "토큰 재발급에 성공하였습니다.");
    }
}