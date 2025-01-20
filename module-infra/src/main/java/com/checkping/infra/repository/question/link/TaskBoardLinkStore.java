package com.checkping.infra.repository.question.link;

import com.checkping.domain.question.TaskBoardLink;

public interface TaskBoardLinkStore {
    TaskBoardLink store(TaskBoardLink taskBoardLink);
}
