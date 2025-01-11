package com.checkping.service.project;

import com.checkping.domain.board.TaskBoard;
import com.checkping.domain.board.TaskBoardComment;
import com.checkping.dto.TaskBoardCommentRequest;
import com.checkping.dto.TaskBoardCommentRequest.RegisterDto;
import com.checkping.dto.TaskBoardCommentResponse;
import com.checkping.exception.project.TaskBoardNotFoundEntityException;
import com.checkping.infra.repository.project.taskboard.TaskBoardReader;
import com.checkping.infra.repository.project.taskboardcomment.TaskBoardCommentStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskBoardCommentServiceImpl implements TaskBoardCommentService {

    private final TaskBoardReader taskBoardReader;
    private final TaskBoardCommentStore taskBoardCommentStore;

    /**
     * 업무 관리 게시글 서비스 - 등록 기능
     *
     * @param taskBoardId 업무 관리 게시글 ID
     * @param request     TaskBoardCommentRequest.RegisterDto 업무 관리 게시글 등록 Dto
     * @return TaskBoardCommentResponse.TaskBoardCommentDto 업무 관리 게시글 등록 결과 Dto
     */
    @Override
    public TaskBoardCommentResponse.TaskBoardCommentDto register(
        Long taskBoardId, RegisterDto request) {

        // find TaskBoard Entity
        TaskBoard taskBoard = taskBoardReader.getTaskBoardById(taskBoardId).orElseThrow(
            TaskBoardNotFoundEntityException::new);

        // Dto -> Entity
        TaskBoardComment initComment = TaskBoardCommentRequest.RegisterDto.toEntity(request,
            taskBoard);
        initComment.activate();

        // save
        TaskBoardComment taskBoardComment = taskBoardCommentStore.store(initComment);

        // Entity -> Dto
        return TaskBoardCommentResponse.TaskBoardCommentDto.toDto(taskBoardComment);
    }
}
