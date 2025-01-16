package com.checkping.infra.repository.project.taskboard;

import com.checkping.domain.board.TaskBoard;
import com.checkping.domain.board.TaskBoard.BoardCategory;
import com.checkping.domain.board.TaskBoard.BoardStatus;
import java.util.List;
import java.util.Optional;

public interface TaskBoardReader {

    List<TaskBoard> getTaskBoard(BoardCategory boardCategory, BoardStatus boardStatus,
        String keyword);

    Optional<TaskBoard> getTaskBoardById(Long id);
}
