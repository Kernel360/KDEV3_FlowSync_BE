package com.checkping.infra.repository.question.link;

import com.checkping.domain.question.QuestionLink;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskBoardLinkStoreImpl implements TaskBoardLinkStore {

    private final TaskBoardLinkRepository taskBoardLinkRepository;

    @Override
    public QuestionLink store(QuestionLink questionLink) {
        return taskBoardLinkRepository.save(questionLink);
    }
}
