package com.checkping.service.question;

import com.checkping.dto.question.QuestionCounter;
import com.checkping.dto.question.QuestionGet;
import com.checkping.dto.question.QuestionRegister;
import com.checkping.dto.question.QuestionRegister.Request;
import com.checkping.dto.question.QuestionRequest.UpdateDto;
import com.checkping.dto.question.QuestionResponse.QuestionItemDto;
import com.checkping.dto.question.QuestionResponse.QuestionListDto;
import com.checkping.dto.question.QuestionSearch;
import com.checkping.dto.question.QuestionSearchCondition;
import java.util.List;

public interface QuestionService {

    QuestionRegister.Response register(Long projectId, Request request, Long questionId);

    QuestionSearch.Response searchQuestions(Long projectId,
        QuestionSearchCondition searchCondition);

    QuestionGet.Response getById(Long projectId, Long questionId);

    QuestionListDto deleteSoft(Long projectId, Long questionId);

    QuestionListDto deleteHard(Long taskBoardId);

    QuestionItemDto update(Long projectId, Long questionId, UpdateDto request);

    List<QuestionCounter.Response> countByProgressStep(Long projectId);

    QuestionGet.Response resolve(Long projectId, Long questionId);
}
