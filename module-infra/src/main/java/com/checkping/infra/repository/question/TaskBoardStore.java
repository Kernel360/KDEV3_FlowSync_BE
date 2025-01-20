package com.checkping.infra.repository.question;

import com.checkping.domain.question.Question;

public interface TaskBoardStore {

    Question store(Question question);

    void deleteHard(Question question);
}
