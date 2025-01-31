package com.checkping.service.question;

import com.checkping.dto.question.QuestionCounter;
import com.checkping.dto.question.QuestionRegister;
import com.checkping.dto.question.QuestionRegister.Request;
import com.checkping.dto.question.QuestionRequest.UpdateDto;
import com.checkping.dto.question.QuestionResponse.QuestionItemDto;
import com.checkping.dto.question.QuestionResponse.QuestionListDto;
import com.checkping.dto.question.QuestionSearch;
import com.checkping.dto.question.QuestionSearchCondition;
import java.util.List;

public interface QuestionService {

    QuestionRegister.Response register(Long projectId, Request request);

    QuestionSearch.Response searchQuestions(Long projectId,
        QuestionSearchCondition searchCondition);

    QuestionItemDto getQuestionById(Long taskBoardId);

    QuestionListDto deleteSoft(Long taskBoardId);

    QuestionListDto deleteHard(Long taskBoardId);

    QuestionItemDto update(Long taskBoardId, UpdateDto request);

    List<QuestionCounter.Response> countByProgressStep(Long projectId);
}
