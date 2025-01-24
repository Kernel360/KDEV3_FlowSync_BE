package com.checkping.api.controller.member;

import com.checkping.dto.member.response.MemberResponseDto;
import com.checkping.service.member.auth.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/me")
    public MemberResponseDto getCurrentMember() {
        return authService.getCurrentMember();
    }
}
