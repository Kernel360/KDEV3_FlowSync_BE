package com.checkping.api.controller;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.TaskBoardRequest;
import com.checkping.dto.TaskBoardResponse;
import com.checkping.service.project.TaskBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TaskBoardController {

    private final TaskBoardService taskBoardService;

    @PostMapping("/posts")
    public BaseResponse<TaskBoardResponse.TaskBoardDto> register(@RequestBody TaskBoardRequest.RegisterDto request) {

        TaskBoardResponse.TaskBoardDto taskBoardDto = taskBoardService.register(request);

        return BaseResponse.success(taskBoardDto);
    }
}
