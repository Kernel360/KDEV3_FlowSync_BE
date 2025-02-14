package com.checkping.infra.repository.question;

import com.checkping.domain.question.Question;
import com.checkping.info.question.QuestionSearchInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionCustomRepository {

    Page<Question> getByCondition(Long projectId,
        QuestionSearchInfo.SearchCondition searchCondition, Pageable pageable);
}
