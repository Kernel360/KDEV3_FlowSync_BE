package com.checkping.service.question.comment;

import com.checkping.dto.question.comment.QuestionCommentRegister;
import com.checkping.dto.question.comment.QuestionCommentRequest;
import com.checkping.dto.question.comment.QuestionCommentResponse.QuestionCommentDto;

public interface QuestionCommentService {

    QuestionCommentDto register(
        Long projectId, QuestionCommentRegister.Request request);

    QuestionCommentDto deleteSoft(Long taskBoardId,
        Long taskBoardCommentId);

    QuestionCommentDto deleteHard(Long taskBoardId,
        Long taskBoardCommentId);

    QuestionCommentDto update(Long taskBoardId, Long taskBoardCommentId,
        QuestionCommentRequest.UpdateDto request);
}
