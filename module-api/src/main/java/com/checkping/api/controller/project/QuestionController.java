package com.checkping.api.controller.project;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.question.QuestionResponse.QuestionListDto;
import com.checkping.dto.question.comment.QuestionCommentRequest;
import com.checkping.dto.question.QuestionRequest;
import com.checkping.dto.question.QuestionRequest.SearchCondition;
import com.checkping.dto.question.QuestionResponse.QuestionItemDto;
import com.checkping.dto.question.comment.QuestionCommentResponse.QuestionCommentDto;
import com.checkping.service.question.comment.QuestionCommentService;
import com.checkping.service.question.QuestionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class QuestionController implements QuestionApi {

    private final QuestionService questionService;
    private final QuestionCommentService questionCommentService;

    @PostMapping(value = "/posts", consumes = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE})
    @Override
    public BaseResponse<QuestionItemDto> register(
        @RequestPart(value = "content") QuestionRequest.RegisterDto request,
        @RequestPart(required = false, value = "fileList") List<MultipartFile> fileList) {

        QuestionItemDto taskBoardDto = questionService.register(request, fileList);

        return BaseResponse.success(taskBoardDto);
    }

    @GetMapping("/posts")
    @Override
    public BaseResponse<List<QuestionListDto>> getTaskBoardList(
        @RequestParam(required = false) String boardCategory,
        @RequestParam(required = false) String boardStatus,
        @RequestParam(required = false) String keyword) {

        // RequestParam -> SearchCondition
        QuestionRequest.SearchCondition searchCondition = new SearchCondition(boardCategory,
            boardStatus, keyword);

        // getTaskBoardList
        List<QuestionListDto> questionListDtoList = questionService.getTaskBoardList(
            searchCondition);

        return BaseResponse.success(questionListDtoList);
    }

    @GetMapping("/posts/{postId}")
    @Override
    public BaseResponse<QuestionItemDto> getTaskBoard(@PathVariable Long postId) {

        QuestionItemDto questionItemDto = questionService.getTaskBoardById(postId);

        return BaseResponse.success(questionItemDto);
    }

    @PutMapping("/posts/{postId}")
    @Override
    public BaseResponse<QuestionItemDto> updateTaskBoard(@PathVariable Long postId,
        @RequestBody QuestionRequest.UpdateDto request) {

        QuestionItemDto updatedBoardDto = questionService.update(postId,
            request);

        return BaseResponse.success(updatedBoardDto);
    }

    @DeleteMapping("/posts/{postId}")
    @Override
    public BaseResponse<QuestionListDto> deleteSoftTaskBoard(
        @PathVariable Long postId) {

        QuestionListDto deletedBoardDto = questionService.deleteSoft(postId);

        return BaseResponse.success(deletedBoardDto);
    }

    @PostMapping("/posts/{postId}/comments")
    @Override
    public BaseResponse<QuestionCommentDto> registerComment(
        @PathVariable Long postId, @RequestBody QuestionCommentRequest.RegisterDto request) {

        QuestionCommentDto questionCommentDto = questionCommentService.register(
            postId, request);

        return BaseResponse.success(questionCommentDto);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    @Override
    public BaseResponse<QuestionCommentDto> deleteSoft(
        @PathVariable Long postId, @PathVariable Long commentId) {

        QuestionCommentDto deletedCommentDto = questionCommentService.deleteSoft(
            postId, commentId);

        return BaseResponse.success(deletedCommentDto);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    @Override
    public BaseResponse<QuestionCommentDto> updateComment(
        @PathVariable Long postId, @PathVariable Long commentId,
        @RequestBody QuestionCommentRequest.UpdateDto request) {

        QuestionCommentDto updatedCommentDto = questionCommentService.update(
            postId, commentId, request);

        return BaseResponse.success(updatedCommentDto);
    }
}
