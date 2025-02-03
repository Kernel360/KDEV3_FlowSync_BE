package com.checkping.api.controller.member;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.member.request.LoginRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "인증 API(AuthApi)", description = "인증과 관련된 API입니다.")
public interface AuthApi {

    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호를 이용하여 로그인을 수행합니다. 성공 시 쿠키에 Access/Refresh 토큰이 저장됩니다."
    )
    BaseResponse<String> login(
            @Parameter(description = "로그인 요청 정보", required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginRequestDto.class)))
            LoginRequestDto request,
            @Parameter(description = "HTTP 응답 객체", hidden = true) HttpServletResponse response
    );

    @Operation(
            summary = "로그아웃",
            description = "로그아웃을 수행하며 Access/Refresh 쿠키를 제거합니다."
    )
    BaseResponse<String> logout(
            @Parameter(description = "HTTP 요청 객체 (쿠키 포함)", hidden = true) HttpServletRequest request,
            @Parameter(description = "HTTP 응답 객체", hidden = true) HttpServletResponse response
    );
}