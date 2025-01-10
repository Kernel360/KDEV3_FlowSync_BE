package com.checkping.infra.repository.project;

import com.checkping.domain.board.TaskBoard;

public interface TaskBoardStore {

    TaskBoard store(TaskBoard taskBoard);
}
