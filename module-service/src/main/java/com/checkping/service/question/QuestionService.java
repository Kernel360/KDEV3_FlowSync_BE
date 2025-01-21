package com.checkping.service.question;

import com.checkping.dto.question.QuestionRegister.Request;
import com.checkping.dto.question.QuestionRequest;
import com.checkping.dto.question.QuestionRequest.UpdateDto;
import com.checkping.dto.question.QuestionResponse.QuestionListDto;
import com.checkping.dto.question.QuestionResponse.QuestionItemDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionService {
    QuestionItemDto register(Request request, List<MultipartFile> fileList);
    List<QuestionListDto> getQuestionList(QuestionRequest.SearchCondition searchCondition);
    QuestionItemDto getQuestionById(Long taskBoardId);
    QuestionListDto deleteSoft (Long taskBoardId);
    QuestionListDto deleteHard(Long taskBoardId);
    QuestionItemDto update(Long taskBoardId, UpdateDto request);
}
