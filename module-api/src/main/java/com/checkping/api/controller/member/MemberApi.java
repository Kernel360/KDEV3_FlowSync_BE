package com.checkping.api.controller.member;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.member.request.ChangePasswordDto;
import com.checkping.dto.member.request.MemberRegisterDto;
import com.checkping.dto.member.request.MemberUpdateDto;
import com.checkping.dto.member.response.MemberListResponseDto;
import com.checkping.dto.member.response.MemberResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.UUID;
import org.springframework.http.MediaType;

@Tag(name = "회원 관리 API(MemberApi)", description = "회원 관리 API입니다.")
public interface MemberApi {

    @Operation(summary = "회원 등록", description = "새로운 회원을 등록하는 기능입니다.")
    BaseResponse<MemberResponseDto> registerMember(
            @Parameter(description = "회원 등록 정보", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            MemberRegisterDto request);

    @Operation(summary = "전체 회원 조회", description = "전체 회원 목록을 조회하는 기능입니다.")
    BaseResponse<MemberListResponseDto> getAllMembers();

    @Operation(summary = "회원 상세 조회", description = "특정 회원의 상세 정보를 조회하는 기능입니다.")
    BaseResponse<MemberResponseDto> getMemberById(
            @Schema(description = "회원 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @Parameter(description = "회원 ID", required = true) UUID memberId);

    @Operation(summary = "회원 정보 수정", description = "특정 회원의 정보를 수정하는 기능입니다.")
    BaseResponse<MemberResponseDto> updateMember(
            @Schema(description = "회원 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @Parameter(description = "회원 ID", required = true) UUID memberId,
            @Parameter(description = "회원 수정 정보", required = true) MemberUpdateDto request);

    @Operation(summary = "회원 비밀번호 변경", description = "특정 회원의 비밀번호를 변경하는 기능입니다.")
    BaseResponse<String> changePassword(
            @Schema(description = "회원 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @Parameter(description = "회원 ID", required = true) UUID memberId,
            @Parameter(description = "비밀번호 변경 정보", required = true) ChangePasswordDto request);

    @Operation(summary = "회원 탈퇴 처리", description = "특정 회원을 탈퇴 처리하는 기능입니다.")
    BaseResponse<String> deleteMember(
            @Schema(description = "회원 ID", example = "123e4567-e89b-12d3-a456-426614174000")
            @Parameter(description = "회원 ID", required = true) UUID memberId,
            //이유 예외처리 예시
            @Schema(description = "탈퇴 사유", example ="{reason : '퇴사로 인한 탈퇴'}")
            @Parameter(description = "탈퇴 사유", required = true) String reason);

}
