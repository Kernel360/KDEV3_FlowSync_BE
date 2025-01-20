package com.checkping.service.question;

import com.checkping.dto.question.TaskBoardRequest;
import com.checkping.dto.question.TaskBoardRequest.RegisterDto;
import com.checkping.dto.question.TaskBoardRequest.UpdateDto;
import com.checkping.dto.question.TaskBoardResponse.TaskBoardItemDto;
import com.checkping.dto.question.TaskBoardResponse.TaskBoardListDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionService {
    TaskBoardItemDto register(RegisterDto request, List<MultipartFile> fileList);
    List<TaskBoardListDto> getTaskBoardList(TaskBoardRequest.SearchCondition searchCondition);
    TaskBoardItemDto getTaskBoardById(Long taskBoardId);
    TaskBoardListDto deleteSoft (Long taskBoardId);
    TaskBoardListDto deleteHard(Long taskBoardId);
    TaskBoardItemDto update(Long taskBoardId, UpdateDto request);
}
