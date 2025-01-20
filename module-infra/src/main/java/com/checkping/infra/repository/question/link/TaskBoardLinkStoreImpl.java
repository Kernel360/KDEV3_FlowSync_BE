package com.checkping.infra.repository.question.link;

import com.checkping.domain.question.TaskBoardLink;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskBoardLinkStoreImpl implements TaskBoardLinkStore {

    private final TaskBoardLinkRepository taskBoardLinkRepository;

    @Override
    public TaskBoardLink store(TaskBoardLink taskBoardLink) {
        return taskBoardLinkRepository.save(taskBoardLink);
    }
}
