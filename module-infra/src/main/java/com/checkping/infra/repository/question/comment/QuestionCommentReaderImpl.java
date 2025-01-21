package com.checkping.infra.repository.question.comment;

import com.checkping.domain.question.QuestionComment;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionCommentReaderImpl implements QuestionCommentReader {

    private final QuestionCommentRepository questionCommentRepository;

    @Override
    public Optional<QuestionComment> getByQuestionCommentId(Long questionCommentId) {
        return questionCommentRepository.findById(questionCommentId);
    }

    /**
     * 업무 관리 게시글 댓글의 게시글 포함 여부
     *
     * @param questionId 업무 관리 게시글 ID
     * @param questionCommentId 업무 관리 게시글 댓글 ID
     * @return 업무 관리 게시글 댓글의 해당 업무 관리 게시글 포함 여부
     */
    @Override
    public boolean checkCommentContaining(Long questionId, Long questionCommentId) {
        return questionCommentRepository.existsByIdAndQuestionId(questionId, questionCommentId);
    }
}
