package com.checkping.infra.repository.question.link;

import com.checkping.domain.question.QuestionLink;
import java.util.List;
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

    @Override
    public List<QuestionLink> store(List<QuestionLink> questionLinks) {
        return questionLinkRepository.saveAll(questionLinks);
    }
}
