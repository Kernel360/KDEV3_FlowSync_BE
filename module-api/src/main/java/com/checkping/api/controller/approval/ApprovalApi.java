package com.checkping.api.controller.approval;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.approval.ApprovalRegister;
import com.checkping.dto.approval.ApprovalRegister.Response;
import com.checkping.dto.approval.ApprovalSearch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Approval API(ApprovalController)", description = "결재 API 입니다.")
public interface ApprovalApi {

    @Operation(summary = "결재 등록", description = "결재를 등록하는 기능입니다.")
    @PostMapping
    BaseResponse<Response> register(
        @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
        @Parameter(description = "결재 등록 정보") @RequestBody ApprovalRegister.Request request);

    @Operation(summary = "결재 목록 조회", description = "결재 목록을 조회하는 기능입니다.")
    BaseResponse<ApprovalSearch.Response> search(
        @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
        @Parameter(description = "질문 게시글 유형") Long progressId,
        @Parameter(description = "질문 게시글 상태") String status,
        @Parameter(description = "질문 게시글 검색어") String keyword,
        @Parameter(description = "현재 페이지") Integer currentPage,
        @Parameter(description = "페이지 사이즈") Integer pageSize);
}
