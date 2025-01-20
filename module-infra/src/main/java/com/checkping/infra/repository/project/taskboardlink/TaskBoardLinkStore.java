package com.checkping.infra.repository.project.taskboardlink;

import com.checkping.domain.question.TaskBoardLink;

public interface TaskBoardLinkStore {
    TaskBoardLink store(TaskBoardLink taskBoardLink);
}
