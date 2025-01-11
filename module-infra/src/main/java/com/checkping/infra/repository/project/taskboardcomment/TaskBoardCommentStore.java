package com.checkping.infra.repository.project.taskboardcomment;

import com.checkping.domain.board.TaskBoardComment;

public interface TaskBoardCommentStore {

    TaskBoardComment store(TaskBoardComment taskBoardComment);

    void deleteHard(TaskBoardComment taskBoardComment);
}
