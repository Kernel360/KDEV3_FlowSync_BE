package com.checkping.infra.repository.question.comment;

import com.checkping.domain.question.TaskBoardComment;
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

    @Override
    public void deleteHard(TaskBoardComment taskBoardComment) {
        taskBoardCommentRepository.delete(taskBoardComment);
    }
}
