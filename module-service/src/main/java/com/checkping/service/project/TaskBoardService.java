package com.checkping.service.project;

import com.checkping.dto.TaskBoardRequest;
import com.checkping.dto.TaskBoardResponse;

public interface TaskBoardService {
    public TaskBoardResponse.BoardDto register(TaskBoardRequest.RegisterDto request);
}
