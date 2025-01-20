package com.checkping.infra.repository.question.comment;

import com.checkping.domain.question.QuestionComment;

public interface TaskBoardCommentStore {

    QuestionComment store(QuestionComment questionComment);

    void deleteHard(QuestionComment questionComment);
}
