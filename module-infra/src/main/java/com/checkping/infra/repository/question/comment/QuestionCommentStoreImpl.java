package com.checkping.infra.repository.question.comment;

import com.checkping.domain.question.QuestionComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionCommentStoreImpl implements QuestionCommentStore {

    private final QuestionCommentRepository questionCommentRepository;

    /**
     * QuestionComment 저장
     *
     * @param questionComment QuestionComment entity
     * @return questionComment 저장 결과
     */
    @Override
    public QuestionComment store(QuestionComment questionComment) {
        return questionCommentRepository.save(questionComment);
    }

    @Override
    public void deleteHard(QuestionComment questionComment) {
        questionCommentRepository.delete(questionComment);
    }
}
