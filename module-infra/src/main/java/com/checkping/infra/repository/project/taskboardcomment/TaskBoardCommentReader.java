package com.checkping.infra.repository.project.taskboardcomment;

import com.checkping.domain.board.TaskBoardComment;
import java.util.Optional;

public interface TaskBoardCommentReader {

    Optional<TaskBoardComment> getByTaskBoardCommentId(Long taskBoardCommentId);

    boolean checkCommentContaining(Long taskBoardId, Long taskBoardCommentId);
}
