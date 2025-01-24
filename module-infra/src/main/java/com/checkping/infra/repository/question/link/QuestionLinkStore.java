package com.checkping.infra.repository.question.link;

import com.checkping.domain.question.QuestionLink;
import java.util.List;

public interface QuestionLinkStore {
    QuestionLink store(QuestionLink questionLink);
    List<QuestionLink> store(List<QuestionLink> questionLinks);
}
