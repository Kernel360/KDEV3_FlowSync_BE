package com.checkping.api.controller.approval;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.approval.ApprovalConfirm;
import com.checkping.dto.approval.ApprovalDelete;
import com.checkping.dto.approval.ApprovalGet;
import com.checkping.dto.approval.ApprovalRegister;
import com.checkping.dto.approval.ApprovalRegister.Response;
import com.checkping.dto.approval.ApprovalSearch;
import com.checkping.dto.approval.ApprovalUpdate;
import com.checkping.dto.approval.comment.ApprovalCommentRegister;
import com.checkping.dto.approval.comment.ApprovalReCommentRegister;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
        @Parameter(description = "현재 페이지") @Min(0) @RequestParam(defaultValue = "1") Integer currentPage,
        @Parameter(description = "페이지 사이즈") @RequestParam(defaultValue = "10") Integer pageSize);

    @Operation(summary = "결재 상세 조회", description = "결재 상세를 조회하는 기능입니다.")
    BaseResponse<ApprovalGet.Response> get(
        @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
        @Parameter(description = "결재 ID") @PathVariable Long approvalId);

    @Operation(summary = "결재 수정", description = "결재를 수정하는 기능입니다.")
    BaseResponse<ApprovalUpdate.Response> update(
        @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
        @Parameter(description = "결재 ID") @PathVariable Long approvalId,
        @Parameter(description = "결재 수정 정보") @RequestBody ApprovalUpdate.Request request);

    @Operation(summary = "결재 삭제", description = "결재를 삭제하는 기능입니다.")
    BaseResponse<ApprovalDelete.Response> delete(
        @Parameter(description = "프로젝트 ID") Long projectId,
        @Parameter(description = "결재 ID") Long approvalId);

    @Operation(summary = "결재 댓글 생성", description = "결재 댓글을 생성하는 기능입니다.")
    BaseResponse<ApprovalCommentRegister.Response> registerComment(
        @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
        @Parameter(description = "결재 ID") @PathVariable Long approvalId,
        @Parameter(description = "결재 댓글 등록 정보") @RequestBody ApprovalCommentRegister.Request request);

    @Operation(summary = "결재 대댓글 생성", description = "결재 대댓글을 생성하는 기능입니다.")
    BaseResponse<ApprovalReCommentRegister.Response> registerReComment(
        @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
        @Parameter(description = "결재 ID") @PathVariable Long approvalId,
        @Parameter(description = "결재 댓글 ID") @PathVariable Long commentId,
        @Parameter(description = "결재 대댓글 등록 정보") @RequestBody ApprovalReCommentRegister.Request request);

    @Operation(summary = "결재 승인", description = "결재를 승인하는 기능입니다.")
    BaseResponse<ApprovalConfirm.Response> confirm(
        @Parameter(description = "프로젝트 ID") @PathVariable Long projectId,
        @Parameter(description = "결재 ID") @PathVariable Long approvalId,
        @Parameter(description = "결재 승인 정보") @RequestBody ApprovalConfirm.Request request);
}
