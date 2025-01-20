package com.checkping.infra.repository.question.comment;

import com.checkping.domain.question.QuestionComment;
import java.util.Optional;

public interface TaskBoardCommentReader {

    Optional<QuestionComment> getByTaskBoardCommentId(Long taskBoardCommentId);

    boolean checkCommentContaining(Long taskBoardId, Long taskBoardCommentId);
}
