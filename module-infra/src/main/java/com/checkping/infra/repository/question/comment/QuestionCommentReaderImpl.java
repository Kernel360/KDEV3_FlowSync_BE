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
    public Optional<QuestionComment> getByTaskBoardCommentId(Long taskBoardCommentId) {
        return questionCommentRepository.findById(taskBoardCommentId);
    }

    /**
     * 업무 관리 게시글 댓글의 게시글 포함 여부
     *
     * @param taskBoardId 업무 관리 게시글 ID
     * @param taskBoardCommentId 업무 관리 게시글 댓글 ID
     * @return 업무 관리 게시글 댓글의 해당 업무 관리 게시글 포함 여부
     */
    @Override
    public boolean checkCommentContaining(Long taskBoardId, Long taskBoardCommentId) {
        return questionCommentRepository.existsByIdAndQuestionId(taskBoardId, taskBoardCommentId);
    }
}
