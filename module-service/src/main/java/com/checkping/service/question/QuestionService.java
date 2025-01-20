package com.checkping.service.question;

import com.checkping.dto.question.QuestionRequest;
import com.checkping.dto.question.QuestionRequest.RegisterDto;
import com.checkping.dto.question.QuestionRequest.UpdateDto;
import com.checkping.dto.question.QuestionResponse.TaskBoardItemDto;
import com.checkping.dto.question.QuestionResponse.TaskBoardListDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionService {
    TaskBoardItemDto register(RegisterDto request, List<MultipartFile> fileList);
    List<TaskBoardListDto> getTaskBoardList(QuestionRequest.SearchCondition searchCondition);
    TaskBoardItemDto getTaskBoardById(Long taskBoardId);
    TaskBoardListDto deleteSoft (Long taskBoardId);
    TaskBoardListDto deleteHard(Long taskBoardId);
    TaskBoardItemDto update(Long taskBoardId, UpdateDto request);
}
