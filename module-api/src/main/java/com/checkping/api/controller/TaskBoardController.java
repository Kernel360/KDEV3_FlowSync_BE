package com.checkping.api.controller;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.TaskBoardRequest;
import com.checkping.dto.TaskBoardRequest.SearchCondition;
import com.checkping.dto.TaskBoardResponse;
import com.checkping.dto.TaskBoardResponse.TaskBoardDto;
import com.checkping.service.project.TaskBoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TaskBoardController {

    private final TaskBoardService taskBoardService;

    @PostMapping("/posts")
    public BaseResponse<TaskBoardResponse.TaskBoardDto> register(
        @RequestBody TaskBoardRequest.RegisterDto request) {

        TaskBoardResponse.TaskBoardDto taskBoardDto = taskBoardService.register(request);

        return BaseResponse.success(taskBoardDto);
    }

    @GetMapping("/posts")
    public BaseResponse<List<TaskBoardDto>> getTaskBoardList(
        @RequestParam(required = false) String boardCategory,
        @RequestParam(required = false) String boardStatus) {

        // RequestParam -> SearchCondition
        TaskBoardRequest.SearchCondition searchCondition = new SearchCondition(boardCategory,
            boardStatus);

        // getTaskBoardList
        List<TaskBoardDto> taskBoardDtoList = taskBoardService.getTaskBoardList(searchCondition);

        return BaseResponse.success(taskBoardDtoList);
    }
}
