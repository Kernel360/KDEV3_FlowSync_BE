package com.checkping.infra.repository.question;

import com.checkping.domain.question.TaskBoard;
import com.checkping.domain.question.TaskBoard.BoardCategory;
import com.checkping.domain.question.TaskBoard.BoardStatus;
import java.util.List;
import java.util.Optional;

public interface TaskBoardReader {

    List<TaskBoard> getTaskBoard(BoardCategory boardCategory, BoardStatus boardStatus,
        String keyword);

    Optional<TaskBoard> getTaskBoardById(Long id);
}
