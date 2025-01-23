package com.checkping.api.controller.project;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.question.QuestionRegister.Request;
import com.checkping.dto.question.QuestionRequest;
import com.checkping.dto.question.QuestionRequest.SearchCondition;
import com.checkping.dto.question.QuestionResponse.QuestionItemDto;
import com.checkping.dto.question.QuestionResponse.QuestionListDto;
import com.checkping.dto.question.comment.QuestionCommentRequest;
import com.checkping.dto.question.comment.QuestionCommentResponse.QuestionCommentDto;
import com.checkping.service.question.QuestionService;
import com.checkping.service.question.comment.QuestionCommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/projects/{projectId}")
@RequiredArgsConstructor
public class QuestionController implements QuestionApi {

    private final QuestionService questionService;
    private final QuestionCommentService questionCommentService;

    @PostMapping(value = "/questions")
    @Override
    public BaseResponse<QuestionItemDto> register(
        @PathVariable Long projectId, @RequestBody Request request) {

        QuestionItemDto taskBoardDto = questionService.register(projectId, request);

        return BaseResponse.success(taskBoardDto);
    }

    @GetMapping("/questions")
    @Override
    public BaseResponse<List<QuestionListDto>> getQuestionList(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String keyword) {

        // RequestParam -> SearchCondition
        QuestionRequest.SearchCondition searchCondition = new SearchCondition(category,
            status, keyword);

        // getTaskBoardList
        List<QuestionListDto> questionListDtoList = questionService.getQuestionList(
            searchCondition);

        return BaseResponse.success(questionListDtoList);
    }

    @GetMapping("/questions/{questionId}")
    @Override
    public BaseResponse<QuestionItemDto> getQuestion(@PathVariable Long questionId) {

        QuestionItemDto questionItemDto = questionService.getQuestionById(questionId);

        return BaseResponse.success(questionItemDto);
    }

    @PutMapping("/questions/{questionId}")
    @Override
    public BaseResponse<QuestionItemDto> updateQuestion(@PathVariable Long questionId,
        @RequestBody QuestionRequest.UpdateDto request) {

        QuestionItemDto updatedBoardDto = questionService.update(questionId,
            request);

        return BaseResponse.success(updatedBoardDto);
    }

    @DeleteMapping("/questions/{questionId}")
    @Override
    public BaseResponse<QuestionListDto> deleteSoftQuestion(
        @PathVariable Long questionId) {

        QuestionListDto deletedBoardDto = questionService.deleteSoft(questionId);

        return BaseResponse.success(deletedBoardDto);
    }

    @PostMapping("/questions/{questionId}/comments")
    @Override
    public BaseResponse<QuestionCommentDto> registerComment(
        @PathVariable Long questionId, @RequestBody QuestionCommentRequest.RegisterDto request) {

        QuestionCommentDto questionCommentDto = questionCommentService.register(
            questionId, request);

        return BaseResponse.success(questionCommentDto);
    }

    @DeleteMapping("/questions/{questionId}/comments/{commentId}")
    @Override
    public BaseResponse<QuestionCommentDto> deleteSoftComment(
        @PathVariable Long questionId, @PathVariable Long commentId) {

        QuestionCommentDto deletedCommentDto = questionCommentService.deleteSoft(
            questionId, commentId);

        return BaseResponse.success(deletedCommentDto);
    }

    @PutMapping("/questions/{questionId}/comments/{commentId}")
    @Override
    public BaseResponse<QuestionCommentDto> updateComment(
        @PathVariable Long questionId, @PathVariable Long commentId,
        @RequestBody QuestionCommentRequest.UpdateDto request) {

        QuestionCommentDto updatedCommentDto = questionCommentService.update(
            questionId, commentId, request);

        return BaseResponse.success(updatedCommentDto);
    }
}
