package com.checkping.infra.repository.project.taskboard;

import com.checkping.domain.board.TaskBoard;
import java.util.List;

public interface TaskBoardReader {
    List<TaskBoard> getTaskBoard(TaskBoard.BoardCategory boardCategory, TaskBoard.BoardStatus boardStatus);
}
