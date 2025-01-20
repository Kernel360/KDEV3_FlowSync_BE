package com.checkping.infra.repository.question.comment;

import com.checkping.domain.question.TaskBoardComment;
import java.util.Optional;

public interface TaskBoardCommentReader {

    Optional<TaskBoardComment> getByTaskBoardCommentId(Long taskBoardCommentId);

    boolean checkCommentContaining(Long taskBoardId, Long taskBoardCommentId);
}
