package com.checkping.service.project;

import com.checkping.domain.board.TaskBoard;
import com.checkping.domain.board.TaskBoardComment;
import com.checkping.dto.TaskBoardCommentRequest;
import com.checkping.dto.TaskBoardCommentRequest.RegisterDto;
import com.checkping.dto.TaskBoardCommentRequest.UpdateDto;
import com.checkping.dto.TaskBoardCommentResponse;
import com.checkping.dto.TaskBoardCommentResponse.TaskBoardCommentDto;
import com.checkping.exception.project.TaskBoardCommentMisMatchEntityException;
import com.checkping.exception.project.TaskBoardCommentNotFoundEntityException;
import com.checkping.exception.project.TaskBoardNotFoundEntityException;
import com.checkping.infra.repository.project.taskboard.TaskBoardReader;
import com.checkping.infra.repository.project.taskboardcomment.TaskBoardCommentReader;
import com.checkping.infra.repository.project.taskboardcomment.TaskBoardCommentStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskBoardCommentServiceImpl implements TaskBoardCommentService {

    private final TaskBoardReader taskBoardReader;
    private final TaskBoardCommentStore taskBoardCommentStore;
    private final TaskBoardCommentReader taskBoardCommentReader;

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

    /**
     * 업무 관리 게시글 댓글 서비스 - 댓글 소프트 삭제
     *
     * @param taskBoardId        업무 관리 게시글 ID
     * @param taskBoardCommentId 업무 관리 게시글 댓글 ID
     * @return 삭제 상태인 TaskBoardComment
     */
    @Override
    public TaskBoardCommentDto deleteSoft(Long taskBoardId, Long taskBoardCommentId) {

        // 업무 관리 게시글에 속한 댓글인지 확인
        boolean isContaining = taskBoardCommentReader.checkCommentContaining(taskBoardId,
            taskBoardCommentId);
        if (!isContaining) {
            throw new TaskBoardCommentMisMatchEntityException();
        }

        // find TaskBoardComment Entity
        TaskBoardComment initComment = taskBoardCommentReader.getByTaskBoardCommentId(
            taskBoardCommentId).orElseThrow(
            TaskBoardCommentNotFoundEntityException::new);

        // soft delete
        initComment.deactivate();

        // save
        TaskBoardComment deletedTaskBoardComment = taskBoardCommentStore.store(initComment);

        // Entity -> Dto
        return TaskBoardCommentResponse.TaskBoardCommentDto.toDto(deletedTaskBoardComment);
    }

    /**
     * 업무 관리 게시글 댓글 서비스 - 댓글 하드 삭제
     *
     * @param taskBoardId        업무 관리 게시글 ID
     * @param taskBoardCommentId 업무 관리 게시글 댓글 ID
     * @return 삭제된 TaskBoardComment
     */
    @Override
    public TaskBoardCommentDto deleteHard(Long taskBoardId, Long taskBoardCommentId) {

        // 업무 관리 게시글에 속한 댓글인지 확인
        boolean isContaining = taskBoardCommentReader.checkCommentContaining(taskBoardId,
            taskBoardCommentId);
        if (!isContaining) {
            throw new TaskBoardCommentMisMatchEntityException();
        }

        // find TaskBoardComment Entity
        TaskBoardComment initComment = taskBoardCommentReader.getByTaskBoardCommentId(
            taskBoardCommentId).orElseThrow(
            TaskBoardCommentNotFoundEntityException::new);

        // HARD DELETE
        taskBoardCommentStore.deleteHard(initComment);

        // Entity -> Dto
        return TaskBoardCommentResponse.TaskBoardCommentDto.toDto(initComment);
    }

    /**
     * 업무 관리 게시판 댓글 서비스 - 수정 기능
     *
     * @param taskBoardId 업무 관리 게시판 ID
     * @param taskBoardCommentId 업무 관리 게시판 댓글 Id
     * @param request TaskBoardCommentRequest.UpdateDto
     * @return 수정된 TaskBoardComment
     */
    @Override
    public TaskBoardCommentDto update(Long taskBoardId, Long taskBoardCommentId,
        UpdateDto request) {

        // 업무 관리 게시글에 속한 댓글인지 확인
        boolean isContaining = taskBoardCommentReader.checkCommentContaining(taskBoardId,
            taskBoardCommentId);
        if (!isContaining) {
            throw new TaskBoardCommentMisMatchEntityException();
        }

        // find TaskBoardComment Entity
        TaskBoardComment initComment = taskBoardCommentReader.getByTaskBoardCommentId(
            taskBoardCommentId).orElseThrow(TaskBoardCommentNotFoundEntityException::new);

        // update
        initComment.update(request.getContent());

        // save
        TaskBoardComment updatedComment = taskBoardCommentStore.store(initComment);

        // Entity -> Dto
        return TaskBoardCommentResponse.TaskBoardCommentDto.toDto(updatedComment);
    }
}
