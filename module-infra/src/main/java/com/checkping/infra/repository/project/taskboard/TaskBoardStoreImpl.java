package com.checkping.infra.repository.project.taskboard;

import com.checkping.domain.board.TaskBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskBoardStoreImpl implements TaskBoardStore {
    private final TaskBoardRepository taskBoardRepository;

    @Override
    public TaskBoard store(TaskBoard taskBoard) {
        return taskBoardRepository.save(taskBoard);
    }
}
