package com.checkping.service.project;

import com.checkping.domain.board.TaskBoard;
import com.checkping.dto.TaskBoardRequest;
import com.checkping.dto.TaskBoardResponse;
import com.checkping.infra.repository.project.TaskBoardStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskBoardServiceImpl implements TaskBoardService {
    private final TaskBoardStore taskBoardStore;

    @Override
    public TaskBoardResponse.BoardDto register(TaskBoardRequest.RegisterDto request) {
        TaskBoard initTaskBoard = TaskBoardRequest.RegisterDto.toEntity(request);
        initTaskBoard.activate();
        TaskBoard taskBoard = taskBoardStore.store(initTaskBoard);
        return TaskBoardResponse.BoardDto.toDto(taskBoard);
    }
}
