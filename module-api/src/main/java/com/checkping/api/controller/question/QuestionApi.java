package com.checkping.api.controller.question;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.question.QuestionCounter.Response;
import com.checkping.dto.question.QuestionGet;
import com.checkping.dto.question.QuestionRegister;
import com.checkping.dto.question.QuestionRequest.UpdateDto;
import com.checkping.dto.question.QuestionResponse.QuestionItemDto;
import com.checkping.dto.question.QuestionResponse.QuestionListDto;
import com.checkping.dto.question.QuestionSearch;
import com.checkping.dto.question.QuestionSearchCondition;
import com.checkping.dto.question.comment.QuestionCommentRegister;
import com.checkping.dto.question.comment.QuestionCommentRequest;
import com.checkping.dto.question.comment.QuestionCommentResponse.QuestionCommentDto;
import com.checkping.dto.question.comment.QuestionReCommentRegister;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;


@Tag(name = "Question API(QuestionController)", description = "질문 게시판 API 입니다.")
public interface QuestionApi {

    @Operation(summary = "질문 게시글 등록", description = "질문 게시글을 등록하는 기능입니다.")
    BaseResponse<QuestionRegister.Response> register(
        @Parameter(description = "프로젝트 ID") Long projectId,
        @Parameter(description = "등록 게시글 정보") QuestionRegister.Request request);

    @Operation(summary = "질문 게시글 답글 등록", description = "질문 게시글에 답글을 등록하는 기능입니다.")
    BaseResponse<QuestionRegister.Response> registerAnswer(
        @Parameter(description = "프로젝트 ID") Long projectId,
        @Parameter(description = "게시글 ID") Long questionId,
        @Parameter(description = "등록 게시글 정보") QuestionRegister.Request request);

    @Operation(summary = "질문 게시글 목록 조회", description = "질문 게시글 목록을 조회하는 기능입니다.")
    BaseResponse<QuestionSearch.Response> searchQuestions(
        @Parameter(description = "프로젝트 ID") Long projectId,
        @Parameter(description = "질문 게시글 검색 조건") @Valid QuestionSearchCondition condition);

    @Operation(summary = "질문 게시글 상세 조회", description = "질문 게시글을 조회하는 기능입니다.")
    BaseResponse<QuestionGet.Response> get(@Parameter(description = "프로젝트 ID") Long projectId,
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
    BaseResponse<QuestionCommentRegister.Response> registerComment(
        @Parameter(description = "프로젝트 ID") Long projectId,
        @Parameter(description = "게시글 ID") Long questionId,
        @Parameter(description = "게시글 댓글 등록 Dto") QuestionCommentRegister.Request request);

    @Operation(summary = "질문 게시글 대댓글 등록", description = "질문 게시글의 대댓글을 등록하는 기능입니다.")
    BaseResponse<QuestionReCommentRegister.Response> registerReComment(
        @Parameter(description = "프로젝트 ID") Long projectId,
        @Parameter(description = "게시글 ID") Long questionId,
        @Parameter(description = "게시글 댓글 ID") Long commentId,
        @Parameter(description = "게시글 대댓글 등록 Dto") QuestionReCommentRegister.Request request);

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

    @Operation(summary = "프로젝트 진행 단계 별 질문 게시글 수 조회", description = "프로젝트 진행 단계 별 질문 게시글 수를 조회하는 기능입니다.")
    BaseResponse<List<Response>> countByProgressStep(
        @Parameter(description = "프로젝트 ID") Long projectId);

    @Operation(summary = "질문 해결 기능", description = "질문을 해결하는 기능입니다.")
    BaseResponse<QuestionGet.Response> resolve(
        @Parameter(description = "프로젝트 ID") Long projectId,
        @Parameter(description = "게시글 ID") Long questionId);
}
