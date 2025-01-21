package com.checkping.service.question.link;

import com.checkping.dto.question.link.QuestionLinkRequest;
import com.checkping.dto.question.link.QuestionLinkResponse.QuestionLinkDto;

public interface QuestionLinkService {

    QuestionLinkDto register(Long taskBoardId,
        QuestionLinkRequest.RegisterDto request);
}
