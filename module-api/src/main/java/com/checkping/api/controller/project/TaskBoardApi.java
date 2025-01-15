package com.checkping.api.controller.project;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.TaskBoardCommentRequest;
import com.checkping.dto.TaskBoardCommentResponse;
import com.checkping.dto.TaskBoardRegister;
import com.checkping.dto.TaskBoardRequest;
import com.checkping.dto.TaskBoardResponse.TaskBoardItemDto;
import com.checkping.dto.TaskBoardResponse.TaskBoardListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "업무 관리 게시판 API(TaskBoardController)", description = "업무 관리 게시판 API 입니다.")
public interface TaskBoardApi {

    @Operation(summary = "업무 관리 게시글 등록", description = "업무 관리 게시글을 등록하는 기능입니다. 파일 첨부도 같이 받습니다.")
    BaseResponse<TaskBoardItemDto> register(
        @Parameter(description = "등록 게시글 정보", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) @RequestPart(value = "content") TaskBoardRegister.Request request,
        @Parameter(description = "등록 첨부 파일", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(required = false, value = "fileList") List<MultipartFile> fileList);

    @Operation(summary = "업무 관리 게시글 목록 조회", description = "업무 관리 게시글 목록을 조회하는 기능입니다.")
    BaseResponse<List<TaskBoardListDto>> getTaskBoardList(
        @Parameter(description = "게시글 유형 - null 가능") String boardCategory,
        @Parameter(description = "게시글 상태 - null 가능") String boardStatus);

    @Operation(summary = "업무 관리 게시글 상세 조회", description = "업무 관리 게시글을 조회하는 기능입니다.")
    BaseResponse<TaskBoardItemDto> getTaskBoard(@Parameter(description = "게시글 ID") Long postId);

    @Operation(summary = "업무 관리 게시글 수정", description = "업무 관리 게시글을 수정하는 기능입니다.")
    BaseResponse<TaskBoardItemDto> updateTaskBoard(@Parameter(description = "게시글 ID") Long postId,
        @Parameter(description = "게시글 수정 정보 Dto") TaskBoardRequest.UpdateDto request);

    @Operation(summary = "업무 관리 게시글 소프트 삭제", description = "업무 관리 게시글을 약한 삭제를 하는 기능입니다..")
    BaseResponse<TaskBoardListDto> deleteSoftTaskBoard(
        @Parameter(description = "게시글 ID") Long postId);

    @Operation(summary = "업무 관리 게시글 댓글 등록", description = "업무 관리 게시글의 댓글을 등록하는 기능입니다.")
    BaseResponse<TaskBoardCommentResponse.TaskBoardCommentDto> registerComment(
        @Parameter(description = "게시글 ID") Long postId,
        @Parameter(description = "게시글 댓글 등록 Dto") TaskBoardCommentRequest.RegisterDto request);

    @Operation(summary = "업무 관리 게시글 댓글 소프트 삭제", description = "업무 관리 게시글의 댓글을 소프트 삭제하는 기능입니다.")
    BaseResponse<TaskBoardCommentResponse.TaskBoardCommentDto> deleteSoft(
        @Parameter(description = "게시글 ID") Long postId,
        @Parameter(description = "게시글 댓글 ID") Long commentId);

    @Operation(summary = "업무 관리 게시글 댓글 수정", description = "업무 관리 게시글의 댓글을 수정하는 기능입니다.")
    BaseResponse<TaskBoardCommentResponse.TaskBoardCommentDto> updateComment(
        @Parameter(description = "게시글 ID") Long postId,
        @Parameter(description = "게시글 댓글 ID") Long commentId,
        @Parameter(description = "게시글 댓글 수정 Dto") TaskBoardCommentRequest.UpdateDto request);
}
