package com.checkping.infra.repository.project.taskboardcomment;

import com.checkping.domain.board.TaskBoardComment;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskBoardCommentReaderImpl implements TaskBoardCommentReader {

    private final TaskBoardCommentRepository taskBoardCommentRepository;

    @Override
    public Optional<TaskBoardComment> getByTaskBoardCommentId(Long taskBoardCommentId) {
        return taskBoardCommentRepository.findById(taskBoardCommentId);
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
        return taskBoardCommentRepository.existsByIdAndTaskBoardId(taskBoardId, taskBoardCommentId);
    }
}
