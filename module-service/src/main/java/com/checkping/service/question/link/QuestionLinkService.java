package com.checkping.service.question.link;

import com.checkping.dto.question.link.QuestionLinkRequest;
import com.checkping.dto.question.link.QuestionLinkResponse.TaskBoardLinkDto;

public interface QuestionLinkService {

    TaskBoardLinkDto register(Long taskBoardId,
        QuestionLinkRequest.RegisterDto request);
}
