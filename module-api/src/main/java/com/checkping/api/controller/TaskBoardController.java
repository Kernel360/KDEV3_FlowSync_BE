package com.checkping.api.controller;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.TaskBoardRequest;
import com.checkping.dto.TaskBoardResponse;
import com.checkping.service.project.TaskBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskBoardController {

    private final TaskBoardService taskBoardService;

    @PostMapping("/posts")
    public BaseResponse<TaskBoardResponse.BoardDto> posts(
        @RequestBody TaskBoardRequest.RegisterDto request) {
        TaskBoardResponse.BoardDto boardDto = taskBoardService.register(request);
        return BaseResponse.success(boardDto);
    }
}
