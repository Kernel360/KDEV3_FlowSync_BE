package com.checkping.service.project;

import com.checkping.dto.TaskBoardRequest;
import com.checkping.dto.TaskBoardResponse;

public interface TaskBoardService {
    TaskBoardResponse.TaskBoardDto register(TaskBoardRequest.RegisterDto request);
}
