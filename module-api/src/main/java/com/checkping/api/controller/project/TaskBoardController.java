package com.checkping.api.controller.project;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.question.comment.TaskBoardCommentRequest;
import com.checkping.dto.question.comment.TaskBoardCommentResponse;
import com.checkping.dto.question.TaskBoardRequest;
import com.checkping.dto.question.TaskBoardRequest.SearchCondition;
import com.checkping.dto.question.TaskBoardResponse;
import com.checkping.dto.question.TaskBoardResponse.TaskBoardItemDto;
import com.checkping.dto.question.TaskBoardResponse.TaskBoardListDto;
import com.checkping.service.question.comment.TaskBoardCommentService;
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
public class TaskBoardController implements TaskBoardApi {

    private final QuestionService questionService;
    private final TaskBoardCommentService taskBoardCommentService;

    @PostMapping(value = "/posts", consumes = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE})
    @Override
    public BaseResponse<TaskBoardItemDto> register(
        @RequestPart(value = "content") TaskBoardRequest.RegisterDto request,
        @RequestPart(required = false, value = "fileList") List<MultipartFile> fileList) {

        TaskBoardItemDto taskBoardDto = questionService.register(request, fileList);

        return BaseResponse.success(taskBoardDto);
    }

    @GetMapping("/posts")
    @Override
    public BaseResponse<List<TaskBoardListDto>> getTaskBoardList(
        @RequestParam(required = false) String boardCategory,
        @RequestParam(required = false) String boardStatus,
        @RequestParam(required = false) String keyword) {

        // RequestParam -> SearchCondition
        TaskBoardRequest.SearchCondition searchCondition = new SearchCondition(boardCategory,
            boardStatus, keyword);

        // getTaskBoardList
        List<TaskBoardListDto> taskBoardListDtoList = questionService.getTaskBoardList(
            searchCondition);

        return BaseResponse.success(taskBoardListDtoList);
    }

    @GetMapping("/posts/{postId}")
    @Override
    public BaseResponse<TaskBoardItemDto> getTaskBoard(@PathVariable Long postId) {

        TaskBoardItemDto taskBoardItemDto = questionService.getTaskBoardById(postId);

        return BaseResponse.success(taskBoardItemDto);
    }

    @PutMapping("/posts/{postId}")
    @Override
    public BaseResponse<TaskBoardItemDto> updateTaskBoard(@PathVariable Long postId,
        @RequestBody TaskBoardRequest.UpdateDto request) {

        TaskBoardResponse.TaskBoardItemDto updatedBoardDto = questionService.update(postId,
            request);

        return BaseResponse.success(updatedBoardDto);
    }

    @DeleteMapping("/posts/{postId}")
    @Override
    public BaseResponse<TaskBoardResponse.TaskBoardListDto> deleteSoftTaskBoard(
        @PathVariable Long postId) {

        TaskBoardResponse.TaskBoardListDto deletedBoardDto = questionService.deleteSoft(postId);

        return BaseResponse.success(deletedBoardDto);
    }

    @PostMapping("/posts/{postId}/comments")
    @Override
    public BaseResponse<TaskBoardCommentResponse.TaskBoardCommentDto> registerComment(
        @PathVariable Long postId, @RequestBody TaskBoardCommentRequest.RegisterDto request) {

        TaskBoardCommentResponse.TaskBoardCommentDto taskBoardCommentDto = taskBoardCommentService.register(
            postId, request);

        return BaseResponse.success(taskBoardCommentDto);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    @Override
    public BaseResponse<TaskBoardCommentResponse.TaskBoardCommentDto> deleteSoft(
        @PathVariable Long postId, @PathVariable Long commentId) {

        TaskBoardCommentResponse.TaskBoardCommentDto deletedCommentDto = taskBoardCommentService.deleteSoft(
            postId, commentId);

        return BaseResponse.success(deletedCommentDto);
    }

    @PutMapping("/posts/{postId}/comments/{commentId}")
    @Override
    public BaseResponse<TaskBoardCommentResponse.TaskBoardCommentDto> updateComment(
        @PathVariable Long postId, @PathVariable Long commentId,
        @RequestBody TaskBoardCommentRequest.UpdateDto request) {

        TaskBoardCommentResponse.TaskBoardCommentDto updatedCommentDto = taskBoardCommentService.update(
            postId, commentId, request);

        return BaseResponse.success(updatedCommentDto);
    }
}
