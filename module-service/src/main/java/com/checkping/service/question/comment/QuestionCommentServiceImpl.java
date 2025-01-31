package com.checkping.service.question.comment;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionComment;
import com.checkping.dto.question.comment.QuestionCommentRegister;
import com.checkping.dto.question.comment.QuestionCommentRequest.UpdateDto;
import com.checkping.dto.question.comment.QuestionCommentResponse.QuestionCommentDto;
import com.checkping.dto.question.comment.QuestionReCommentRegister;
import com.checkping.dto.question.comment.QuestionReCommentRegister.Request;
import com.checkping.exception.question.QuestionNotFoundEntityException;
import com.checkping.exception.question.comment.QuestionCommentMisMatchEntityException;
import com.checkping.exception.question.comment.QuestionCommentNotFoundEntityException;
import com.checkping.infra.repository.question.QuestionReader;
import com.checkping.infra.repository.question.comment.QuestionCommentReader;
import com.checkping.infra.repository.question.comment.QuestionCommentStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public QuestionCommentRegister.Response register(
        Long projectId, QuestionCommentRegister.Request request) {

        // find Question Entity
        Question question = questionReader.getQuestionById(projectId).orElseThrow(
            QuestionNotFoundEntityException::new);

        // Dto -> Entity
        QuestionComment initComment = QuestionCommentRegister.Request.toEntity(request, question);

        // save
        QuestionComment comment = questionCommentStore.store(initComment);

        // Entity -> Dto
        return QuestionCommentRegister.Response.toDto(comment);
    }

    /**
     * 질문 게시글 댓글 서비스 - 대댓글 등록
     *
     * @param projectId  프로젝트 ID
     * @param questionId 질문 ID
     * @param commentId  질문 게시글 댓글 ID
     * @param request    QuestionReCommentRegister.Request 업무 관리 게시글 댓글 등록 Dto
     * @return QuestionReCommentRegister.Response 업무 관리 게시글 댓글 등록 결과 Dto
     */
    @Transactional
    @Override
    public QuestionReCommentRegister.Response registerReComment(Long projectId, Long questionId,
        Long commentId, Request request) {

        // Check Project contain Question
        containingProject(projectId, questionId);

        // find Question Entity
        Question question = questionReader.getQuestionById(questionId).orElseThrow(
            QuestionNotFoundEntityException::new);

        // Check Question contain Comment
        containingComment(question.getId(), commentId);

        // find Parent Comment Entity
        QuestionComment parentComment = questionCommentReader.getByQuestionCommentId(
            commentId).orElseThrow(QuestionCommentNotFoundEntityException::new);

        // Dto -> Entity
        QuestionComment initReComment = QuestionReCommentRegister.Request.toEntity(request,
            question, parentComment);

        // Save Entity
        QuestionComment reComment = questionCommentStore.store(initReComment);

        // Entity -> Dto
        return QuestionReCommentRegister.Response.toDto(reComment);
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

        // Check Project contain Comment(Question)
        containingComment(taskBoardId, taskBoardCommentId);

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

        // Check Project contain Comment(Question)
        containingComment(taskBoardId, taskBoardCommentId);

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

        // Check Project contain Comment(Question)
        containingComment(taskBoardId, taskBoardCommentId);

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

    /**
     * 프로젝트 ID와 질문 ID로 질문 포함 여부 확인
     *
     * @param projectId  프로젝트 ID
     * @param questionId 질문 Id
     */
    private void containingProject(Long projectId, Long questionId) {
        boolean isContaining = questionReader.checkQuestionContaining(projectId, questionId);
        if (!isContaining) {
            throw new QuestionNotFoundEntityException();
        }
    }

    /**
     * 질문 게시글 프로젝트 ID와 댓글 ID로 댓글 포함 여부 확인
     *
     * @param questionId        질문 Id
     * @param questionCommentId 질문 댓글 Id
     * @return 댓글 포함 여부(boolean
     */
    private void containingComment(Long questionId, Long questionCommentId) {
        boolean isContaining = questionCommentReader.checkCommentContaining(questionId,
            questionCommentId);
        if (!isContaining) {
            throw new QuestionCommentMisMatchEntityException();
        }
    }
}
