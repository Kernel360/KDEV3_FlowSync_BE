package com.checkping.service.project;

import com.checkping.dto.TaskBoardRequest;
import com.checkping.dto.TaskBoardResponse;
import com.checkping.dto.TaskBoardResponse.TaskBoardDto;
import java.util.List;

public interface TaskBoardService {
    TaskBoardResponse.TaskBoardDto register(TaskBoardRequest.RegisterDto request);
    List<TaskBoardDto> getTaskBoardList(TaskBoardRequest.SearchCondition searchCondition);
}
