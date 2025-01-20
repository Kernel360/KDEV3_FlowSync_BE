package com.checkping.infra.repository.project.taskboard;

import com.checkping.domain.question.TaskBoard;

public interface TaskBoardStore {

    TaskBoard store(TaskBoard taskBoard);

    void deleteHard(TaskBoard taskBoard);
}
