package com.checkping.infra.repository.question;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.Question.Category;
import com.checkping.domain.question.Question.Status;
import java.util.List;
import java.util.Optional;

public interface QuestionReader {

    List<Question> getQuestion(Category category, Status status,
        String keyword);

    Optional<Question> getQuestionById(Long id);
}
