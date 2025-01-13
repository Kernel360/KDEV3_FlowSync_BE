package com.checkping.infra.repository.project.taskboardlink;

import com.checkping.domain.board.TaskBoardLink;
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
