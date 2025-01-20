package com.checkping.service.question.comment;

import com.checkping.dto.question.comment.TaskBoardCommentRequest;
import com.checkping.dto.question.comment.TaskBoardCommentRequest.RegisterDto;
import com.checkping.dto.question.comment.TaskBoardCommentResponse;

public interface QuestionCommentService {

    TaskBoardCommentResponse.TaskBoardCommentDto register(
        Long taskBoardId, RegisterDto request);

    TaskBoardCommentResponse.TaskBoardCommentDto deleteSoft(Long taskBoardId,
        Long taskBoardCommentId);

    TaskBoardCommentResponse.TaskBoardCommentDto deleteHard(Long taskBoardId,
        Long taskBoardCommentId);

    TaskBoardCommentResponse.TaskBoardCommentDto update(Long taskBoardId, Long taskBoardCommentId,
        TaskBoardCommentRequest.UpdateDto request);
}
