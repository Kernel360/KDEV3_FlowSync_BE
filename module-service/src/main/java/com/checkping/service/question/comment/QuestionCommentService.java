package com.checkping.service.question.comment;

import com.checkping.dto.question.comment.QuestionCommentRegister;
import com.checkping.dto.question.comment.QuestionCommentRequest;
import com.checkping.dto.question.comment.QuestionCommentResponse.QuestionCommentDto;
import com.checkping.dto.question.comment.QuestionReCommentRegister;

public interface QuestionCommentService {

    QuestionCommentRegister.Response register(
        Long projectId, QuestionCommentRegister.Request request);

    QuestionReCommentRegister.Response registerReComment(
        Long projectId, Long commentId,
        QuestionReCommentRegister.Request request);

    QuestionCommentDto deleteSoft(Long taskBoardId,
        Long taskBoardCommentId);

    QuestionCommentDto deleteHard(Long taskBoardId,
        Long taskBoardCommentId);

    QuestionCommentDto update(Long taskBoardId, Long taskBoardCommentId,
        QuestionCommentRequest.UpdateDto request);
}
