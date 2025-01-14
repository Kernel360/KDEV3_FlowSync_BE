package com.checkping.api.controller;

import com.checkping.service.member.auth.ReissueService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReissueController {

    private final ReissueService reissueService;

    public ReissueController(ReissueService reissueService) {
        this.reissueService = reissueService;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        try {
            // Get refresh token
            String refresh = reissueService.validateAndExtractRefreshToken(request.getCookies());
            reissueService.checkTokenValidity(refresh);

            // Extract email and role
            String email = reissueService.getEmailFromToken(refresh);
            String role = reissueService.getRoleFromToken(refresh);

            // Generate new tokens
            String newAccess = reissueService.generateAccessToken(email, role);
            String newRefresh = reissueService.generateRefreshToken(email, role);

            // Set response
            response.setHeader("Authorization", "Bearer " + newAccess);
            response.addCookie(createCookie("refresh", newRefresh));

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        return cookie;
    }
}