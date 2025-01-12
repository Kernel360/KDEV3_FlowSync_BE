package com.checkping.infra.repository.project.taskboard;

import com.checkping.domain.board.TaskBoard;
import java.util.List;
import java.util.Optional;

public interface TaskBoardReader {
    List<TaskBoard> getTaskBoard(TaskBoard.BoardCategory boardCategory, TaskBoard.BoardStatus boardStatus);
    Optional<TaskBoard> getTaskBoardById(Long id);
}
