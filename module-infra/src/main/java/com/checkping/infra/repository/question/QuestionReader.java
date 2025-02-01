package com.checkping.infra.repository.question;

import com.checkping.domain.question.Question;
import com.checkping.info.question.QuestionSearchInfo;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface QuestionReader {

    Page<Question> searchQuestions(Long projectId, QuestionSearchInfo.SearchCondition searchCondition);

    Optional<Question> getQuestionById(Long id);

    Long countQuestionsByProject(Long projectId);

    Long countQuestionsByProgressStep(Long projectId, Long progressStepId);

    boolean checkQuestionContaining(Long projectId, Long questionId);
}
