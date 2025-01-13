package com.checkping.service.project;

import com.checkping.dto.TaskBoardLinkRequest;
import com.checkping.dto.TaskBoardLinkResponse.TaskBoardLinkDto;

public interface TaskBoardLinkService {

    TaskBoardLinkDto register(Long taskBoardId,
        TaskBoardLinkRequest.RegisterDto request);
}
