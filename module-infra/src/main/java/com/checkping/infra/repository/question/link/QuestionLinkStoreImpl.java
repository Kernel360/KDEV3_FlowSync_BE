package com.checkping.infra.repository.question.link;

import com.checkping.domain.question.QuestionLink;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionLinkStoreImpl implements QuestionLinkStore {

    private final QuestionLinkRepository questionLinkRepository;

    @Override
    public QuestionLink store(QuestionLink questionLink) {
        return questionLinkRepository.save(questionLink);
    }
}
