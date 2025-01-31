package com.checkping.service.question.comment;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionComment;
import com.checkping.dto.question.comment.QuestionCommentRegister;
import com.checkping.dto.question.comment.QuestionCommentRequest.UpdateDto;
import com.checkping.dto.question.comment.QuestionCommentResponse.QuestionCommentDto;
import com.checkping.exception.question.QuestionNotFoundEntityException;
import com.checkping.exception.question.comment.QuestionCommentMisMatchEntityException;
import com.checkping.exception.question.comment.QuestionCommentNotFoundEntityException;
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
     * @param projectId 질문 게시글 ID
     * @param request   QuestionCommentRegister.Request 업무 관리 게시글 등록 Dto
     * @return QuestionCommentResponse.QuestionCommentDto 업무 관리 게시글 등록 결과 Dto
     */
    @Override
    public QuestionCommentDto register(
        Long projectId, QuestionCommentRegister.Request request) {

        // find Question Entity
        Question question = questionReader.getQuestionById(projectId).orElseThrow(
            QuestionNotFoundEntityException::new);

        // Dto -> Entity
        QuestionComment initComment = QuestionCommentRegister.Request.toEntity(request, question);

        // save
        QuestionComment questionComment = questionCommentStore.store(initComment);

        // Entity -> Dto
        return QuestionCommentDto.toDto(questionComment);
    }

    /**
     * 업무 관리 게시글 댓글 서비스 - 댓글 소프트 삭제
     *
     * @param taskBoardId        업무 관리 게시글 ID
     * @param taskBoardCommentId 업무 관리 게시글 댓글 ID
     * @return 삭제 상태인 QuestionComment
     */
    @Override
    public QuestionCommentDto deleteSoft(Long taskBoardId, Long taskBoardCommentId) {

        // 업무 관리 게시글에 속한 댓글인지 확인
        boolean isContaining = questionCommentReader.checkCommentContaining(taskBoardId,
            taskBoardCommentId);
        if (!isContaining) {
            throw new QuestionCommentMisMatchEntityException();
        }

        // find QuestionComment Entity
        QuestionComment initComment = questionCommentReader.getByQuestionCommentId(
            taskBoardCommentId).orElseThrow(
            QuestionCommentNotFoundEntityException::new);

        // soft delete
        initComment.deactivate();

        // save
        QuestionComment deletedQuestionComment = questionCommentStore.store(initComment);

        // Entity -> Dto
        return QuestionCommentDto.toDto(deletedQuestionComment);
    }

    /**
     * 업무 관리 게시글 댓글 서비스 - 댓글 하드 삭제
     *
     * @param taskBoardId        업무 관리 게시글 ID
     * @param taskBoardCommentId 업무 관리 게시글 댓글 ID
     * @return 삭제된 QuestionComment
     */
    @Override
    public QuestionCommentDto deleteHard(Long taskBoardId, Long taskBoardCommentId) {

        // 업무 관리 게시글에 속한 댓글인지 확인
        boolean isContaining = questionCommentReader.checkCommentContaining(taskBoardId,
            taskBoardCommentId);
        if (!isContaining) {
            throw new QuestionCommentMisMatchEntityException();
        }

        // find QuestionComment Entity
        QuestionComment initComment = questionCommentReader.getByQuestionCommentId(
            taskBoardCommentId).orElseThrow(
            QuestionCommentNotFoundEntityException::new);

        // HARD DELETE
        questionCommentStore.deleteHard(initComment);

        // Entity -> Dto
        return QuestionCommentDto.toDto(initComment);
    }

    /**
     * 업무 관리 게시판 댓글 서비스 - 수정 기능
     *
     * @param taskBoardId        업무 관리 게시판 ID
     * @param taskBoardCommentId 업무 관리 게시판 댓글 Id
     * @param request            QuestionCommentRequest.UpdateDto
     * @return 수정된 QuestionComment
     */
    @Override
    public QuestionCommentDto update(Long taskBoardId, Long taskBoardCommentId,
        UpdateDto request) {

        // 업무 관리 게시글에 속한 댓글인지 확인
        boolean isContaining = questionCommentReader.checkCommentContaining(taskBoardId,
            taskBoardCommentId);
        if (!isContaining) {
            throw new QuestionCommentMisMatchEntityException();
        }

        // find QuestionComment Entity
        QuestionComment initComment = questionCommentReader.getByQuestionCommentId(
            taskBoardCommentId).orElseThrow(QuestionCommentNotFoundEntityException::new);

        // update
        initComment.update(request.getContent());

        // save
        QuestionComment updatedComment = questionCommentStore.store(initComment);

        // Entity -> Dto
        return QuestionCommentDto.toDto(updatedComment);
    }
}
