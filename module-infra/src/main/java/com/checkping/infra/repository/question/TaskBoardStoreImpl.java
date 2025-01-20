package com.checkping.infra.repository.question;

import com.checkping.domain.question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskBoardStoreImpl implements TaskBoardStore {
    private final TaskBoardRepository taskBoardRepository;

    @Override
    public Question store(Question question) {
        return taskBoardRepository.save(question);
    }

    /**
     * HARD DELETE - Question
     *
     * @param question 삭제할 Question 엔티티
     */
    @Override
    public void deleteHard(Question question) {
        taskBoardRepository.delete(question);
    }
}
