package com.checkping.service.project;

import com.checkping.dto.TaskBoardRequest;
import com.checkping.dto.TaskBoardResponse.TaskBoardItemDto;
import com.checkping.dto.TaskBoardResponse.TaskBoardListDto;
import java.util.List;

public interface TaskBoardService {
    TaskBoardItemDto register(TaskBoardRequest.RegisterDto request);
    List<TaskBoardListDto> getTaskBoardList(TaskBoardRequest.SearchCondition searchCondition);
    TaskBoardItemDto getTaskBoardById(Long taskBoardId);
}
