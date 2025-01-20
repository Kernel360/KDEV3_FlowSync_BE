package com.checkping.service.question.comment;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionComment;
import com.checkping.dto.question.comment.TaskBoardCommentRequest;
import com.checkping.dto.question.comment.TaskBoardCommentRequest.RegisterDto;
import com.checkping.dto.question.comment.TaskBoardCommentRequest.UpdateDto;
import com.checkping.dto.question.comment.TaskBoardCommentResponse;
import com.checkping.dto.question.comment.TaskBoardCommentResponse.TaskBoardCommentDto;
import com.checkping.exception.question.comment.QuestionCommentMisMatchEntityException;
import com.checkping.exception.question.comment.QuestionCommentNotFoundEntityException;
import com.checkping.exception.question.QuestionNotFoundEntityException;
import com.checkping.infra.repository.question.QuestionReader;
import com.checkping.infra.repository.question.comment.QuestionCommentReader;
import com.checkping.infra.repository.question.comment.QuestionCommentStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionCommentServiceImpl implements QuestionCommentService {

    private final QuestionReader questionReader;
    private final QuestionCommentStore questionCommentStore;
    private final QuestionCommentReader questionCommentReader;

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

        // find Question Entity
        Question question = questionReader.getTaskBoardById(taskBoardId).orElseThrow(
            QuestionNotFoundEntityException::new);

        // Dto -> Entity
        QuestionComment initComment = TaskBoardCommentRequest.RegisterDto.toEntity(request,
            question);
        initComment.activate();

        // save
        QuestionComment questionComment = questionCommentStore.store(initComment);

        // Entity -> Dto
        return TaskBoardCommentResponse.TaskBoardCommentDto.toDto(questionComment);
    }

    /**
     * 업무 관리 게시글 댓글 서비스 - 댓글 소프트 삭제
     *
     * @param taskBoardId        업무 관리 게시글 ID
     * @param taskBoardCommentId 업무 관리 게시글 댓글 ID
     * @return 삭제 상태인 QuestionComment
     */
    @Override
    public TaskBoardCommentDto deleteSoft(Long taskBoardId, Long taskBoardCommentId) {

        // 업무 관리 게시글에 속한 댓글인지 확인
        boolean isContaining = questionCommentReader.checkCommentContaining(taskBoardId,
            taskBoardCommentId);
        if (!isContaining) {
            throw new QuestionCommentMisMatchEntityException();
        }

        // find QuestionComment Entity
        QuestionComment initComment = questionCommentReader.getByTaskBoardCommentId(
            taskBoardCommentId).orElseThrow(
            QuestionCommentNotFoundEntityException::new);

        // soft delete
        initComment.deactivate();

        // save
        QuestionComment deletedQuestionComment = questionCommentStore.store(initComment);

        // Entity -> Dto
        return TaskBoardCommentResponse.TaskBoardCommentDto.toDto(deletedQuestionComment);
    }

    /**
     * 업무 관리 게시글 댓글 서비스 - 댓글 하드 삭제
     *
     * @param taskBoardId        업무 관리 게시글 ID
     * @param taskBoardCommentId 업무 관리 게시글 댓글 ID
     * @return 삭제된 QuestionComment
     */
    @Override
    public TaskBoardCommentDto deleteHard(Long taskBoardId, Long taskBoardCommentId) {

        // 업무 관리 게시글에 속한 댓글인지 확인
        boolean isContaining = questionCommentReader.checkCommentContaining(taskBoardId,
            taskBoardCommentId);
        if (!isContaining) {
            throw new QuestionCommentMisMatchEntityException();
        }

        // find QuestionComment Entity
        QuestionComment initComment = questionCommentReader.getByTaskBoardCommentId(
            taskBoardCommentId).orElseThrow(
            QuestionCommentNotFoundEntityException::new);

        // HARD DELETE
        questionCommentStore.deleteHard(initComment);

        // Entity -> Dto
        return TaskBoardCommentResponse.TaskBoardCommentDto.toDto(initComment);
    }

    /**
     * 업무 관리 게시판 댓글 서비스 - 수정 기능
     *
     * @param taskBoardId 업무 관리 게시판 ID
     * @param taskBoardCommentId 업무 관리 게시판 댓글 Id
     * @param request TaskBoardCommentRequest.UpdateDto
     * @return 수정된 QuestionComment
     */
    @Override
    public TaskBoardCommentDto update(Long taskBoardId, Long taskBoardCommentId,
        UpdateDto request) {

        // 업무 관리 게시글에 속한 댓글인지 확인
        boolean isContaining = questionCommentReader.checkCommentContaining(taskBoardId,
            taskBoardCommentId);
        if (!isContaining) {
            throw new QuestionCommentMisMatchEntityException();
        }

        // find QuestionComment Entity
        QuestionComment initComment = questionCommentReader.getByTaskBoardCommentId(
            taskBoardCommentId).orElseThrow(QuestionCommentNotFoundEntityException::new);

        // update
        initComment.update(request.getContent());

        // save
        QuestionComment updatedComment = questionCommentStore.store(initComment);

        // Entity -> Dto
        return TaskBoardCommentResponse.TaskBoardCommentDto.toDto(updatedComment);
    }
}
