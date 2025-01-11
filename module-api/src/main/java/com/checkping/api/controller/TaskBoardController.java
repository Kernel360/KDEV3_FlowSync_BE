package com.checkping.api.controller;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.TaskBoardCommentRequest;
import com.checkping.dto.TaskBoardCommentResponse;
import com.checkping.dto.TaskBoardRequest;
import com.checkping.dto.TaskBoardRequest.SearchCondition;
import com.checkping.dto.TaskBoardResponse.TaskBoardItemDto;
import com.checkping.dto.TaskBoardResponse.TaskBoardListDto;
import com.checkping.service.project.TaskBoardCommentService;
import com.checkping.service.project.TaskBoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TaskBoardController {

    private final TaskBoardService taskBoardService;
    private final TaskBoardCommentService taskBoardCommentService;

    @PostMapping("/posts")
    public BaseResponse<TaskBoardItemDto> register(
        @RequestBody TaskBoardRequest.RegisterDto request) {

        TaskBoardItemDto taskBoardDto = taskBoardService.register(request);

        return BaseResponse.success(taskBoardDto);
    }

    @GetMapping("/posts")
    public BaseResponse<List<TaskBoardListDto>> getTaskBoardList(
        @RequestParam(required = false) String boardCategory,
        @RequestParam(required = false) String boardStatus) {

        // RequestParam -> SearchCondition
        TaskBoardRequest.SearchCondition searchCondition = new SearchCondition(boardCategory,
            boardStatus);

        // getTaskBoardList
        List<TaskBoardListDto> taskBoardListDtoList = taskBoardService.getTaskBoardList(
            searchCondition);

        return BaseResponse.success(taskBoardListDtoList);
    }

    @GetMapping("/posts/{postId}")
    public BaseResponse<TaskBoardItemDto> getTaskBoard(@PathVariable Long postId) {

        TaskBoardItemDto taskBoardItemDto = taskBoardService.getTaskBoardById(postId);

        return BaseResponse.success(taskBoardItemDto);
    }

    @PostMapping("/post/{postId}/comment")
    public BaseResponse<TaskBoardCommentResponse.TaskBoardCommentDto> registerComment(
        @PathVariable Long postId, @RequestBody TaskBoardCommentRequest.RegisterDto request) {

        TaskBoardCommentResponse.TaskBoardCommentDto taskBoardCommentDto = taskBoardCommentService.register(
            postId, request);

        return BaseResponse.success(taskBoardCommentDto);
    }
}
