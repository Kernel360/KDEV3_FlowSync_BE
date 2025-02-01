package com.checkping.api.controller.question;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.question.QuestionCounter;
import com.checkping.dto.question.QuestionCounter.Response;
import com.checkping.dto.question.QuestionRegister;
import com.checkping.dto.question.QuestionRegister.Request;
import com.checkping.dto.question.QuestionRequest.UpdateDto;
import com.checkping.dto.question.QuestionResponse.QuestionItemDto;
import com.checkping.dto.question.QuestionResponse.QuestionListDto;
import com.checkping.dto.question.QuestionSearch;
import com.checkping.dto.question.QuestionSearchCondition;
import com.checkping.dto.question.comment.QuestionCommentRegister;
import com.checkping.dto.question.comment.QuestionCommentRequest;
import com.checkping.dto.question.comment.QuestionCommentResponse.QuestionCommentDto;
import com.checkping.dto.question.comment.QuestionReCommentRegister;
import com.checkping.service.question.QuestionService;
import com.checkping.service.question.comment.QuestionCommentService;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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
@Validated
@RestController
@RequestMapping("/projects/{projectId}/questions")
@RequiredArgsConstructor
public class QuestionController implements QuestionApi {

    private final QuestionService questionService;
    private final QuestionCommentService questionCommentService;

    @PostMapping
    @Override
    public BaseResponse<QuestionRegister.Response> register(
        @PathVariable Long projectId, @RequestBody Request request) {

        QuestionRegister.Response response = questionService.register(projectId, request);

        return BaseResponse.success(response);
    }

    @GetMapping
    @Override
    public BaseResponse<QuestionSearch.Response> searchQuestions(
        @PathVariable Long projectId,
        @RequestParam(required = false) Long progressId,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String keyword,
        @Min(0) @RequestParam(defaultValue = "1") Integer currentPage,
        @RequestParam(defaultValue = "10") Integer pageSize) {

        // Create QuestionSearchCondition
        QuestionSearchCondition searchCondition = new QuestionSearchCondition(progressId, status,
            keyword, currentPage, pageSize);

        // Search Questions
        QuestionSearch.Response response = questionService.searchQuestions(projectId,
            searchCondition);

        return BaseResponse.success(response);
    }

    @GetMapping("/{questionId}")
    @Override
    public BaseResponse<QuestionItemDto> get(@PathVariable Long projectId,
        @PathVariable Long questionId) {

        QuestionItemDto questionItemDto = questionService.getQuestionById(questionId);

        return BaseResponse.success(questionItemDto);
    }

    @PutMapping("/{questionId}")
    @Override
    public BaseResponse<QuestionItemDto> updateQuestion(@PathVariable Long projectId,
        @PathVariable Long questionId,
        @RequestBody UpdateDto request) {

        QuestionItemDto updatedBoardDto = questionService.update(questionId,
            request);

        return BaseResponse.success(updatedBoardDto);
    }

    @DeleteMapping("/{questionId}")
    @Override
    public BaseResponse<QuestionListDto> deleteSoftQuestion(
        @PathVariable Long projectId, @PathVariable Long questionId) {

        QuestionListDto deletedBoardDto = questionService.deleteSoft(questionId);

        return BaseResponse.success(deletedBoardDto);
    }

    @PostMapping("/{questionId}/comments")
    @Override
    public BaseResponse<QuestionCommentRegister.Response> registerComment(
        @PathVariable Long projectId, @PathVariable Long questionId,
        @RequestBody QuestionCommentRegister.Request request) {

        QuestionCommentRegister.Response response = questionCommentService.register(
            questionId, request);

        return BaseResponse.success(response);
    }

    @Override
    @PostMapping("/{questionId}/comments/{commentId}/recomments")
    public BaseResponse<QuestionReCommentRegister.Response> registerReComment(
        @PathVariable Long projectId,
        @PathVariable Long questionId, @PathVariable Long commentId,
        @RequestBody QuestionReCommentRegister.Request request) {

        QuestionReCommentRegister.Response response = questionCommentService.registerReComment(
            projectId, questionId, commentId, request);

        return BaseResponse.success(response);
    }

    @DeleteMapping("/{questionId}/comments/{commentId}")
    @Override
    public BaseResponse<QuestionCommentDto> deleteSoftComment(
        @PathVariable Long projectId, @PathVariable Long questionId, @PathVariable Long commentId) {

        QuestionCommentDto deletedCommentDto = questionCommentService.deleteSoft(
            questionId, commentId);

        return BaseResponse.success(deletedCommentDto);
    }

    @PutMapping("/{questionId}/comments/{commentId}")
    @Override
    public BaseResponse<QuestionCommentDto> updateComment(
        @PathVariable Long projectId, @PathVariable Long questionId, @PathVariable Long commentId,
        @RequestBody QuestionCommentRequest.UpdateDto request) {

        QuestionCommentDto updatedCommentDto = questionCommentService.update(
            questionId, commentId, request);

        return BaseResponse.success(updatedCommentDto);
    }

    @GetMapping("/counts")
    @Override
    public BaseResponse<List<QuestionCounter.Response>> countByProgressStep(
        @PathVariable Long projectId) {

        List<Response> response = questionService.countByProgressStep(projectId);

        return BaseResponse.success(response);
    }
}
