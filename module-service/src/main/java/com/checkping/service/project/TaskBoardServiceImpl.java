package com.checkping.service.project;

import com.checkping.domain.board.TaskBoard;
import com.checkping.dto.TaskBoardRequest;
import com.checkping.dto.TaskBoardRequest.SearchCondition;
import com.checkping.dto.TaskBoardResponse;
import com.checkping.dto.TaskBoardResponse.TaskBoardDto;
import com.checkping.infra.repository.project.taskboard.TaskBoardReader;
import com.checkping.infra.repository.project.taskboard.TaskBoardStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskBoardServiceImpl implements TaskBoardService {

    private final TaskBoardStore taskBoardStore;
    private final TaskBoardReader taskBoardReader;

    /**
     * 업무 관리 게시글 등록하기
     *
     * @param request 업무 관리 게시글에 필요한 request
     * @return 생성한 TaskBoard 의 Dto
     */
    @Override
    public TaskBoardResponse.TaskBoardDto register(TaskBoardRequest.RegisterDto request) {

        // dto -> entity
        TaskBoard initTaskBoard = TaskBoardRequest.RegisterDto.toEntity(request);
        initTaskBoard.activate();

        // save entity
        TaskBoard taskBoard = taskBoardStore.store(initTaskBoard);

        // entity -> dto
        return TaskBoardResponse.TaskBoardDto.toDto(taskBoard);
    }

    /**
     * TaskBoard 조회 하기 (게시글 유형, 게시글 상태 별 필터링)
     *
     * @param searchCondition RequestParam 에서 받아오는 String 을 관리하는 타입
     * @return 조회한 TaskBoardDto 의 리스트
     */
    @Override
    public List<TaskBoardDto> getTaskBoardList(SearchCondition searchCondition) {

        // 조회
        List<TaskBoard> taskBoardList = taskBoardReader.getTaskBoard(
            searchCondition.getBoardCategory(),
            searchCondition.getBoardStatus());

        // TaskBoard -> TaskBoardDto
        return taskBoardList.stream().map(TaskBoardDto::toDto).toList();
    }
}
