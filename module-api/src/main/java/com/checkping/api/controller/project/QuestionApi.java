package com.checkping.api.controller.project;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.question.QuestionResponse.QuestionListDto;
import com.checkping.dto.question.comment.QuestionCommentRequest;
import com.checkping.dto.question.QuestionRequest;
import com.checkping.dto.question.QuestionResponse.QuestionItemDto;
import com.checkping.dto.question.comment.QuestionCommentResponse.QuestionCommentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "Question API(QuestionController)", description = "질문 게시판 API 입니다.")
public interface QuestionApi {

    @Operation(summary = "질문 게시글 등록", description = "질문 게시글을 등록하는 기능입니다.")
    BaseResponse<QuestionItemDto> register(
        @Parameter(description = "등록 게시글 정보", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) @RequestPart(value = "content") QuestionRequest.RegisterDto request,
        @Parameter(description = "등록 첨부 파일", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(required = false, value = "fileList") List<MultipartFile> fileList);

    @Operation(summary = "질문 게시글 목록 조회", description = "질문 게시글 목록을 조회하는 기능입니다.")
    BaseResponse<List<QuestionListDto>> getQuestionList(
        @Parameter(description = "게시글 유형 - null 가능") String category,
        @Parameter(description = "게시글 상태 - null 가능") String status,
        @Parameter(description = "게시글 검색어")String keyword);

    @Operation(summary = "질문 게시글 상세 조회", description = "질문 게시글을 조회하는 기능입니다.")
    BaseResponse<QuestionItemDto> getQuestion(@Parameter(description = "게시글 ID") Long postId);

    @Operation(summary = "질문 게시글 수정", description = "질문 게시글을 수정하는 기능입니다.")
    BaseResponse<QuestionItemDto> updateQuestion(@Parameter(description = "게시글 ID") Long postId,
        @Parameter(description = "게시글 수정 정보 Dto") QuestionRequest.UpdateDto request);

    @Operation(summary = "질문 게시글 소프트 삭제", description = "질문 게시글을 약한 삭제를 하는 기능입니다..")
    BaseResponse<QuestionListDto> deleteSoftQuestion(
        @Parameter(description = "게시글 ID") Long postId);

    @Operation(summary = "질문 게시글 댓글 등록", description = "질문 게시글의 댓글을 등록하는 기능입니다.")
    BaseResponse<QuestionCommentDto> registerComment(
        @Parameter(description = "게시글 ID") Long postId,
        @Parameter(description = "게시글 댓글 등록 Dto") QuestionCommentRequest.RegisterDto request);

    @Operation(summary = "질문 게시글 댓글 소프트 삭제", description = "질문 게시글의 댓글을 소프트 삭제하는 기능입니다.")
    BaseResponse<QuestionCommentDto> deleteSoftComment(
        @Parameter(description = "게시글 ID") Long postId,
        @Parameter(description = "게시글 댓글 ID") Long commentId);

    @Operation(summary = "질문 게시글 댓글 수정", description = "질문 게시글의 댓글을 수정하는 기능입니다.")
    BaseResponse<QuestionCommentDto> updateComment(
        @Parameter(description = "게시글 ID") Long postId,
        @Parameter(description = "게시글 댓글 ID") Long commentId,
        @Parameter(description = "게시글 댓글 수정 Dto") QuestionCommentRequest.UpdateDto request);
}
