package com.checkping.api.controller.member.auth;

import com.checkping.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "토큰 재발급 Reissue API", description = "토큰 재발급 API입니다.")
public interface ReissueApi {

    @Operation(
            summary = "토큰 재발급",
            description = "Access Token과 Refresh Token을 재발급하는 기능입니다."
    )
    @PostMapping("/reissue")
    BaseResponse<?> reissue(HttpServletRequest request, HttpServletResponse response);
}