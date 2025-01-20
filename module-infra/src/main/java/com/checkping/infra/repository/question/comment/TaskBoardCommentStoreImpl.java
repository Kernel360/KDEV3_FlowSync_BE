package com.checkping.infra.repository.question.comment;

import com.checkping.domain.question.QuestionComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskBoardCommentStoreImpl implements TaskBoardCommentStore {

    private final TaskBoardCommentRepository taskBoardCommentRepository;

    /**
     * QuestionComment 저장
     *
     * @param questionComment QuestionComment entity
     * @return questionComment 저장 결과
     */
    @Override
    public QuestionComment store(QuestionComment questionComment) {
        return taskBoardCommentRepository.save(questionComment);
    }

    @Override
    public void deleteHard(QuestionComment questionComment) {
        taskBoardCommentRepository.delete(questionComment);
    }
}
