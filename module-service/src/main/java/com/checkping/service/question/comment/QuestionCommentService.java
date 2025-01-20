package com.checkping.service.question.comment;

import com.checkping.dto.question.comment.QuestionCommentRequest;
import com.checkping.dto.question.comment.QuestionCommentRequest.RegisterDto;
import com.checkping.dto.question.comment.QuestionCommentResponse;

public interface QuestionCommentService {

    QuestionCommentResponse.TaskBoardCommentDto register(
        Long taskBoardId, RegisterDto request);

    QuestionCommentResponse.TaskBoardCommentDto deleteSoft(Long taskBoardId,
        Long taskBoardCommentId);

    QuestionCommentResponse.TaskBoardCommentDto deleteHard(Long taskBoardId,
        Long taskBoardCommentId);

    QuestionCommentResponse.TaskBoardCommentDto update(Long taskBoardId, Long taskBoardCommentId,
        QuestionCommentRequest.UpdateDto request);
}
