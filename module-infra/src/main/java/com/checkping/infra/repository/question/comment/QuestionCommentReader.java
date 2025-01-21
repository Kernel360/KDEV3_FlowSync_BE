package com.checkping.infra.repository.question.comment;

import com.checkping.domain.question.QuestionComment;
import java.util.Optional;

public interface QuestionCommentReader {

    Optional<QuestionComment> getByQuestionCommentId(Long questionCommentId);

    boolean checkCommentContaining(Long taskBoardId, Long taskBoardCommentId);
}
