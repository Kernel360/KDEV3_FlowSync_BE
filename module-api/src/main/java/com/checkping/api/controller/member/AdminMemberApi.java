package com.checkping.api.controller.member;

import com.checkping.common.dto.PageInfo;
import com.checkping.common.response.BaseResponse;
import com.checkping.dto.ProjectListGet;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "어드민 회원 관리 API(AdminMemberApi)", description = "회원 관리 API입니다.")
public interface AdminMemberApi {

    @Operation(summary = "회원 등록", description = "새로운 회원을 등록하는 기능입니다.")
    BaseResponse<MemberResponseDto> registerMember(
        @Parameter(description = "회원 등록 정보", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
        MemberRegisterDto request);

    @Operation(summary = "전체 회원 조회", description = "페이징 지원을 포함한 전체 회원 목록을 조회하는 기능입니다.")
    BaseResponse<MemberListResponseDto> getAllMembers(
        @Parameter(description = "페이지 번호 (1부터 시작)", example = "1", required = true)
        int page,
        @Parameter(description = "페이지 크기", example = "10", required = true)
        int size,
        @Parameter(description = "role(예: ADMIN, MEMBER)", example = "ADMIN")
        String role,
        @Parameter(description = "status(예: ACTIVE, INACTIVE, DELETED)", example = "ACTIVE")
        String status,
        @Parameter(description = "검색어 (이름/이메일 검색)", example = "홍길동")
        String keyword,
        @Parameter(description = "정렬 필드 - 예시: id(기본), name, email, reg_at, modified_at, role", example = "name")
        String sortField,
        @Parameter(description = "정렬 방향 (ASC, DESC)", example = "DESC")
        String sortDirection);

    @Operation(summary = "회원 상세 조회", description = "특정 회원의 상세 정보를 조회하는 기능입니다.")
    BaseResponse<MemberResponseDto> getMemberById(
        @Schema(description = "회원 ID", example = "1")
        @Parameter(description = "회원 ID", required = true) Long memberId);

    @Operation(summary = "회원 정보 수정", description = "특정 회원의 정보를 수정하는 기능입니다.")
    BaseResponse<MemberResponseDto> updateMember(
        @Schema(description = "회원 ID", example = "1")
        @Parameter(description = "회원 ID", required = true) Long memberId,
        @Parameter(description = "회원 수정 정보", required = true) MemberUpdateDto request);

    @Operation(summary = "회원 비밀번호 변경", description = "특정 회원의 비밀번호를 변경하는 기능입니다.")
    BaseResponse<String> changePassword(
        @Schema(description = "회원 ID", example = "1")
        @Parameter(description = "회원 ID", required = true) Long memberId,
        @Parameter(description = "비밀번호 변경 정보", required = true) ChangePasswordDto request);

    @Operation(summary = "회원 탈퇴 처리", description = "특정 회원을 탈퇴 처리하는 기능입니다.")
    BaseResponse<String> deleteMember(
        @Schema(description = "회원 ID", example = "1")
        @Parameter(description = "회원 ID", required = true) Long memberId,
        //이유 예외처리 예시
        @Schema(description = "탈퇴 사유", example = "퇴사로 인한 탈퇴")
        @Parameter(description = "탈퇴 사유", required = true) String reason);

    @Operation(summary = "소속 업체별 회원 조회", description = "특정 소속 업체의 회원 목록을 조회하는 기능입니다.")
    BaseResponse<MemberListResponseDto> getMembersByOrganizationId(
        @Schema(description = "소속 업체 ID", example = "1")
        @Parameter(description = "소속 업체 ID", required = true) Long organizationId,
        @Parameter(description = "페이지 번호 (1부터 시작)", example = "1", required = true) int page,
        @Parameter(description = "페이지 크기", example = "10", required = true) int size);

    @Operation(summary = "회원 활성화 처리", description = "비활성화되거나 삭제된 회원을 활성화 처리하는 기능입니다.")
    BaseResponse<String> activateMember(
        @Schema(description = "회원 ID", example = "1")
        @Parameter(description = "회원 ID", required = true) Long memberId);

    @Operation(summary = "회원 비활성화 처리", description = "활성화된 회원을 비활성화 처리하는 기능입니다.")
    BaseResponse<String> deactivateMember(
        @Schema(description = "회원 ID", example = "1")
        @Parameter(description = "회원 ID", required = true) Long memberId);

    @Operation(summary = "회원 참여중인 프로젝트 목록", description = "회원이 속한 프로젝트 목록을 조회합니다.")
    BaseResponse<PageInfo.Response<ProjectListGet.Response>> getProjectsByMember(
            @Parameter(description = "회원 ID") @PathVariable Long memberId,
            @Parameter(description = "프로젝트 관리단계(CONTRACT / IN_PROGRESS / COMPLETED / MAINTENANCE / PAUSED / DELETED)") @RequestParam(required = false) String managementStep,
            @Parameter(description = "페이지 번호") @RequestParam int currentPage,
            @Parameter(description = "게시글 수") @RequestParam int pageSize,
            @Parameter(description = "검색어") @RequestParam String keyword
    );
}