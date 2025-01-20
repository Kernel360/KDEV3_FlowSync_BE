package com.checkping.service.question.link;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionLink;
import com.checkping.dto.question.link.TaskBoardLinkRequest;
import com.checkping.dto.question.link.TaskBoardLinkResponse.TaskBoardLinkDto;
import com.checkping.exception.question.TaskBoardNotFoundEntityException;
import com.checkping.infra.repository.question.QuestionReader;
import com.checkping.infra.repository.question.link.QuestionLinkStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskBoardLinkServiceImpl implements TaskBoardLinkService {

    private final QuestionLinkStore questionLinkStore;
    private final QuestionReader questionReader;

    /**
     * 업무 관리 게시글 첨부 링크 서비스 - 등록
     *
     * @param taskBoardId 업무 관리 게시글 ID
     * @param request     TaskBoardLinkRequest.RegisterDto 업무 관리 게시글 첨부 링크 Dto
     * @return 업무 관리 게시글 - 첨부 링크 DTO
     */
    @Override
    public TaskBoardLinkDto register(Long taskBoardId,
        TaskBoardLinkRequest.RegisterDto request) {

        // find Question Entity
        Question question = questionReader.getTaskBoardById(taskBoardId).orElseThrow(
            TaskBoardNotFoundEntityException::new);

        // Dto -> Entity
        QuestionLink initQuestionLink = TaskBoardLinkRequest.RegisterDto.toEntity(question,
            request);

        // save
        QuestionLink questionLink = questionLinkStore.store(initQuestionLink);

        // Entity -> Dto
        return TaskBoardLinkDto.toDto(questionLink);
    }
}
