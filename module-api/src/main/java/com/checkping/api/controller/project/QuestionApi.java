package com.checkping.api.controller.project;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.question.QuestionRegister;
import com.checkping.dto.question.QuestionRegister.Request;
import com.checkping.dto.question.QuestionRequest.UpdateDto;
import com.checkping.dto.question.QuestionResponse.QuestionItemDto;
import com.checkping.dto.question.QuestionResponse.QuestionListDto;
import com.checkping.dto.question.QuestionSearch;
import com.checkping.dto.question.comment.QuestionCommentRequest;
import com.checkping.dto.question.comment.QuestionCommentRequest.RegisterDto;
import com.checkping.dto.question.comment.QuestionCommentResponse.QuestionCommentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Question API(QuestionController)", description = "질문 게시판 API 입니다.")
public interface QuestionApi {

    @Operation(summary = "질문 게시글 등록", description = "질문 게시글을 등록하는 기능입니다.")
    BaseResponse<QuestionRegister.Response> register(
        @Parameter(description = "프로젝트 ID") Long projectId,
        @Parameter(description = "등록 게시글 정보") Request request);

    @Operation(summary = "질문 게시글 목록 조회", description = "질문 게시글 목록을 조회하는 기능입니다.")
    BaseResponse<QuestionSearch.Response> searchQuestions(
        @Parameter(description = "프로젝트 ID") Long projectId,
        @Parameter(description = "질문 게시글 유형") String category,
        @Parameter(description = "질문 게시글 상태") String status,
        @Parameter(description = "질문 게시글 검색어") String keyword,
        @Parameter(description = "현재 페이지") Integer currentPage,
        @Parameter(description = "페이지 사이즈") Integer pageSize);

    @Operation(summary = "질문 게시글 상세 조회", description = "질문 게시글을 조회하는 기능입니다.")
    BaseResponse<QuestionItemDto> getQuestion(@Parameter(description = "프로젝트 ID") Long projectId,
        @Parameter(description = "게시글 ID") Long questionId);

    @Operation(summary = "질문 게시글 수정", description = "질문 게시글을 수정하는 기능입니다.")
    BaseResponse<QuestionItemDto> updateQuestion(@Parameter(description = "프로젝트 ID") Long projectId,
        @Parameter(description = "게시글 ID") Long questionId,
        @Parameter(description = "게시글 수정 정보 Dto") UpdateDto request);

    @Operation(summary = "질문 게시글 소프트 삭제", description = "질문 게시글을 약한 삭제를 하는 기능입니다..")
    BaseResponse<QuestionListDto> deleteSoftQuestion(
        @Parameter(description = "프로젝트 ID") Long projectId,
        @Parameter(description = "게시글 ID") Long questionId);

    @Operation(summary = "질문 게시글 댓글 등록", description = "질문 게시글의 댓글을 등록하는 기능입니다.")
    BaseResponse<QuestionCommentDto> registerComment(
        @Parameter(description = "프로젝트 ID") Long projectId,
        @Parameter(description = "게시글 ID") Long questionId,
        @Parameter(description = "게시글 댓글 등록 Dto") RegisterDto request);

    @Operation(summary = "질문 게시글 댓글 소프트 삭제", description = "질문 게시글의 댓글을 소프트 삭제하는 기능입니다.")
    BaseResponse<QuestionCommentDto> deleteSoftComment(
        @Parameter(description = "프로젝트 ID") Long projectId,
        @Parameter(description = "게시글 ID") Long questionId,
        @Parameter(description = "게시글 댓글 ID") Long commentId);

    @Operation(summary = "질문 게시글 댓글 수정", description = "질문 게시글의 댓글을 수정하는 기능입니다.")
    BaseResponse<QuestionCommentDto> updateComment(
        @Parameter(description = "프로젝트 ID") Long projectId,
        @Parameter(description = "게시글 ID") Long questionId,
        @Parameter(description = "게시글 댓글 ID") Long commentId,
        @Parameter(description = "게시글 댓글 수정 Dto") QuestionCommentRequest.UpdateDto request);
}
