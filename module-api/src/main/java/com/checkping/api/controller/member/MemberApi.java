package com.checkping.api.controller.member;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.member.response.MemberSignatureResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "회원 관리 API(MemberApi)", description = "회원 관리 API입니다.")
public interface MemberApi {

    @Operation(summary = "회원 서명 업로드", description = "회원의 서명 이미지를 업로드하는 기능입니다.")
    BaseResponse<MemberSignatureResponseDto> uploadSignature(
        @Schema(description = "회원 ID", example = "1")
        @Parameter(description = "회원 ID", required = true) Long memberId,
        @Parameter(description = "서명 이미지 파일", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) MultipartFile multipartFile);
}
