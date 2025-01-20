package com.checkping.infra.repository.question.link;

import com.checkping.domain.question.QuestionLink;

public interface TaskBoardLinkStore {
    QuestionLink store(QuestionLink questionLink);
}
