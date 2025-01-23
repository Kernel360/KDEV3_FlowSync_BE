package com.checkping.service.question;

import com.checkping.dto.question.QuestionRegister.Request;
import com.checkping.dto.question.QuestionRequest;
import com.checkping.dto.question.QuestionRequest.UpdateDto;
import com.checkping.dto.question.QuestionResponse.QuestionListDto;
import com.checkping.dto.question.QuestionResponse.QuestionItemDto;
import java.util.List;

public interface QuestionService {
    QuestionItemDto register(Long projectId, Request request);
    List<QuestionListDto> getQuestionList(QuestionRequest.SearchCondition searchCondition);
    QuestionItemDto getQuestionById(Long taskBoardId);
    QuestionListDto deleteSoft (Long taskBoardId);
    QuestionListDto deleteHard(Long taskBoardId);
    QuestionItemDto update(Long taskBoardId, UpdateDto request);
}
