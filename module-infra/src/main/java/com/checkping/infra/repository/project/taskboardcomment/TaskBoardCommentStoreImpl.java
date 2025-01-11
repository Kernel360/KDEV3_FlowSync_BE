package com.checkping.infra.repository.project.taskboardcomment;

import com.checkping.domain.board.TaskBoardComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskBoardCommentStoreImpl implements TaskBoardCommentStore {

    private final TaskBoardCommentRepository taskBoardCommentRepository;

    /**
     * TaskBoardComment 저장
     *
     * @param taskBoardComment TaskBoardComment entity
     * @return taskBoardComment 저장 결과
     */
    @Override
    public TaskBoardComment store(TaskBoardComment taskBoardComment) {
        return taskBoardCommentRepository.save(taskBoardComment);
    }
}
