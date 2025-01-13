package com.checkping.service.project;

import com.checkping.dto.TaskBoardCommentRequest.RegisterDto;
import com.checkping.dto.TaskBoardCommentResponse;

public interface TaskBoardCommentService {

    TaskBoardCommentResponse.TaskBoardCommentDto register(
        Long taskBoardId, RegisterDto request);

    TaskBoardCommentResponse.TaskBoardCommentDto deleteSoft(Long taskBoardId,
        Long taskBoardCommentId);

    TaskBoardCommentResponse.TaskBoardCommentDto deleteHard(Long taskBoardId,
        Long taskBoardCommentId);
}
