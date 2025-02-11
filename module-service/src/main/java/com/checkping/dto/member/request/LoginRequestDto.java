package com.checkping.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    @Schema(description = "이메일", example = "test@test.com")
    private String email;
    @Schema(description = "비밀번호", example = "1111")
    private String password;
}