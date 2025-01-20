package com.checkping.service.question.link;

import com.checkping.domain.question.TaskBoard;
import com.checkping.domain.question.TaskBoardLink;
import com.checkping.dto.question.link.TaskBoardLinkRequest;
import com.checkping.dto.question.link.TaskBoardLinkResponse.TaskBoardLinkDto;
import com.checkping.exception.question.TaskBoardNotFoundEntityException;
import com.checkping.infra.repository.question.TaskBoardReader;
import com.checkping.infra.repository.question.link.TaskBoardLinkStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskBoardLinkServiceImpl implements TaskBoardLinkService {

    private final TaskBoardLinkStore taskBoardLinkStore;
    private final TaskBoardReader taskBoardReader;

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

        // find TaskBoard Entity
        TaskBoard taskBoard = taskBoardReader.getTaskBoardById(taskBoardId).orElseThrow(
            TaskBoardNotFoundEntityException::new);

        // Dto -> Entity
        TaskBoardLink initTaskBoardLink = TaskBoardLinkRequest.RegisterDto.toEntity(taskBoard,
            request);

        // save
        TaskBoardLink taskBoardLink = taskBoardLinkStore.store(initTaskBoardLink);

        // Entity -> Dto
        return TaskBoardLinkDto.toDto(taskBoardLink);
    }
}
