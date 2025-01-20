package com.checkping.service.question.link;

import com.checkping.dto.question.link.TaskBoardLinkRequest;
import com.checkping.dto.question.link.TaskBoardLinkResponse.TaskBoardLinkDto;

public interface TaskBoardLinkService {

    TaskBoardLinkDto register(Long taskBoardId,
        TaskBoardLinkRequest.RegisterDto request);
}
