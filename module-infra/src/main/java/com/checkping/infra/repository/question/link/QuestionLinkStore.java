package com.checkping.infra.repository.question.link;

import com.checkping.domain.question.QuestionLink;

public interface QuestionLinkStore {
    QuestionLink store(QuestionLink questionLink);
}
