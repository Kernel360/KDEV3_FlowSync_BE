package com.checkping.infra.repository.project.taskboardlink;

import com.checkping.domain.board.TaskBoardLink;

public interface TaskBoardLinkStore {
    TaskBoardLink store(TaskBoardLink taskBoardLink);
}
