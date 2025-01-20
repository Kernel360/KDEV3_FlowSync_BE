package com.checkping.service.question.link;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionLink;
import com.checkping.dto.question.link.QuestionLinkRequest;
import com.checkping.dto.question.link.QuestionLinkResponse.TaskBoardLinkDto;
import com.checkping.exception.question.QuestionNotFoundEntityException;
import com.checkping.infra.repository.question.QuestionReader;
import com.checkping.infra.repository.question.link.QuestionLinkStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionLinkServiceImpl implements QuestionLinkService {

    private final QuestionLinkStore questionLinkStore;
    private final QuestionReader questionReader;

    /**
     * 업무 관리 게시글 첨부 링크 서비스 - 등록
     *
     * @param taskBoardId 업무 관리 게시글 ID
     * @param request     QuestionLinkRequest.RegisterDto 업무 관리 게시글 첨부 링크 Dto
     * @return 업무 관리 게시글 - 첨부 링크 DTO
     */
    @Override
    public TaskBoardLinkDto register(Long taskBoardId,
        QuestionLinkRequest.RegisterDto request) {

        // find Question Entity
        Question question = questionReader.getTaskBoardById(taskBoardId).orElseThrow(
            QuestionNotFoundEntityException::new);

        // Dto -> Entity
        QuestionLink initQuestionLink = QuestionLinkRequest.RegisterDto.toEntity(question,
            request);

        // save
        QuestionLink questionLink = questionLinkStore.store(initQuestionLink);

        // Entity -> Dto
        return TaskBoardLinkDto.toDto(questionLink);
    }
}
