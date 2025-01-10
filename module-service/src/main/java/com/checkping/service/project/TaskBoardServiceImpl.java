package com.checkping.service.project;

import com.checkping.domain.board.TaskBoard;
import com.checkping.dto.TaskBoardRequest;
import com.checkping.dto.TaskBoardResponse;
import com.checkping.infra.repository.project.TaskBoardStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskBoardServiceImpl implements TaskBoardService {

    private final TaskBoardStore taskBoardStore;

    /**
     * 업무 관리 게시글 등록하기
     *
     * @param request 업무 관리 게시글에 필요한 request
     * @return 생성한 TaskBoard 의 Dto
     */
    @Override
    public TaskBoardResponse.TaskBoardDto register(TaskBoardRequest.RegisterDto request) {

        TaskBoard initTaskBoard = TaskBoardRequest.RegisterDto.toEntity(request);
        initTaskBoard.activate();

        TaskBoard taskBoard = taskBoardStore.store(initTaskBoard);

        return TaskBoardResponse.TaskBoardDto.toDto(taskBoard);
    }
}
