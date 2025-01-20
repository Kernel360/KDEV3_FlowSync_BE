package com.checkping.infra.repository.question;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.Question.BoardCategory;
import com.checkping.domain.question.Question.BoardStatus;
import java.util.List;
import java.util.Optional;

public interface QuestionReader {

    List<Question> getTaskBoard(BoardCategory boardCategory, BoardStatus boardStatus,
        String keyword);

    Optional<Question> getTaskBoardById(Long id);
}
