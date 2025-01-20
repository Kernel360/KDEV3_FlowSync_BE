package com.checkping.infra.repository.question.comment;

import com.checkping.domain.question.TaskBoardComment;

public interface TaskBoardCommentStore {

    TaskBoardComment store(TaskBoardComment taskBoardComment);

    void deleteHard(TaskBoardComment taskBoardComment);
}
