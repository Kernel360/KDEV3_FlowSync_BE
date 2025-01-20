package com.checkping.infra.repository.question;

import com.checkping.domain.question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionStoreImpl implements QuestionStore {
    private final QuestionRepository questionRepository;

    @Override
    public Question store(Question question) {
        return questionRepository.save(question);
    }

    /**
     * HARD DELETE - Question
     *
     * @param question 삭제할 Question 엔티티
     */
    @Override
    public void deleteHard(Question question) {
        questionRepository.delete(question);
    }
}
