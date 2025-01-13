package com.checkping.service.project;

import com.checkping.domain.board.TaskBoard;
import com.checkping.domain.board.TaskBoardComment;
import com.checkping.domain.board.TaskBoardLink;
import com.checkping.dto.TaskBoardLinkRequest;
import com.checkping.dto.TaskBoardRequest;
import com.checkping.dto.TaskBoardRequest.SearchCondition;
import com.checkping.dto.TaskBoardRequest.UpdateDto;
import com.checkping.dto.TaskBoardResponse;
import com.checkping.dto.TaskBoardResponse.TaskBoardItemDto;
import com.checkping.dto.TaskBoardResponse.TaskBoardListDto;
import com.checkping.exception.project.TaskBoardNotFoundEntityException;
import com.checkping.infra.repository.project.taskboard.TaskBoardReader;
import com.checkping.infra.repository.project.taskboard.TaskBoardStore;
import com.checkping.infra.repository.project.taskboardcomment.TaskBoardCommentReader;
import com.checkping.infra.repository.project.taskboardcomment.TaskBoardCommentStore;
import com.checkping.infra.repository.project.taskboardlink.TaskBoardLinkStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskBoardServiceImpl implements TaskBoardService {

    private final TaskBoardStore taskBoardStore;
    private final TaskBoardReader taskBoardReader;
    private final TaskBoardCommentReader taskBoardCommentReader;
    private final TaskBoardCommentStore taskBoardCommentStore;
    private final TaskBoardLinkStore taskBoardLinkStore;

    /**
     * 업무 관리 게시글 등록하기
     *
     * @param request 업무 관리 게시글에 필요한 request
     * @return 생성한 TaskBoard 의 Dto
     */
    @Override
    public TaskBoardItemDto register(TaskBoardRequest.RegisterDto request) {

        // dto -> entity
        TaskBoard initTaskBoard = TaskBoardRequest.RegisterDto.toEntity(request);
        initTaskBoard.activate();

        // save TaskBoard entity
        TaskBoard taskBoard = taskBoardStore.store(initTaskBoard);

        // get register info
        List<TaskBoardLinkRequest.RegisterDto> linkDtoList = request.getTaskBoardLinkList();

        // loop for add
        for (TaskBoardLinkRequest.RegisterDto linkDto : linkDtoList) {

            // TaskBoardLink Dto -> Entity
            TaskBoardLink initTaskBoardLink = TaskBoardLinkRequest.RegisterDto.toEntity(taskBoard, linkDto);

            // save TaskBoardLink
            TaskBoardLink taskBoardLink = taskBoardLinkStore.store(initTaskBoardLink);

            // ADD TaskBoardLink (in TaskBoard)
            taskBoard.addLink(taskBoardLink);
        }

        // Entity -> Dto
        return TaskBoardItemDto.toDto(taskBoard);
    }

    /**
     * TaskBoard 조회 하기 (게시글 유형, 게시글 상태 별 필터링)
     *
     * @param searchCondition RequestParam 에서 받아오는 String 을 관리하는 타입
     * @return 조회한 TaskBoardListDto 의 리스트
     */
    @Override
    public List<TaskBoardListDto> getTaskBoardList(SearchCondition searchCondition) {

        // 조회
        List<TaskBoard> taskBoardList = taskBoardReader.getTaskBoard(
            searchCondition.getBoardCategory(),
            searchCondition.getBoardStatus());

        // TaskBoard -> TaskBoardListDto
        return taskBoardList.stream().map(TaskBoardListDto::toDto).toList();
    }

    /**
     * 업무 관리 게시글 서비스 - 상세 조회
     *
     * @param taskBoardId 업무 관리 게시글 ID
     * @return TaskBoardListDto
     */
    @Override
    public TaskBoardItemDto getTaskBoardById(Long taskBoardId) {

        // find TaskBoard Entity
        TaskBoard taskBoard = taskBoardReader.getTaskBoardById(taskBoardId).orElseThrow(
            TaskBoardNotFoundEntityException::new);

        // Entity -> Dto
        return TaskBoardItemDto.toDto(taskBoard);
    }

    /**
     * 업무 관리 게시글 서비스 - SOFT DELETE
     *
     * @param taskBoardId 업무 관리 게시글 ID
     * @return 상태 변경이 된 업무 관리 게시글 Dto
     */
    @Override
    public TaskBoardListDto deleteSoft(Long taskBoardId) {

        // find TaskBoard Entity
        TaskBoard initTaskBoard = taskBoardReader.getTaskBoardById(taskBoardId).orElseThrow(
            TaskBoardNotFoundEntityException::new);

        // TaskBoardComment - SOFT DELETE
        List<TaskBoardComment> commentList = initTaskBoard.getCommentList();
        for (TaskBoardComment comment : commentList) {
            comment.deactivate();
            taskBoardCommentStore.store(comment);
        }

        // TaskBoard - SOFT DELETE
        initTaskBoard.deactivate();

        // save
        TaskBoard deletedTaskBoard = taskBoardStore.store(initTaskBoard);

        // Entity -> Dto
        return TaskBoardResponse.TaskBoardListDto.toDto(deletedTaskBoard);
    }

    /**
     * 업무 관리 게시글 서비스 - HARD DELETE
     *
     * @param taskBoardId 업무 관리 게시글 ID
     * @return HARD DELETE 를 요청한 업무 관리 게시글 Dto
     */
    @Override
    public TaskBoardListDto deleteHard(Long taskBoardId) {

        // find TaskBoard Entity
        TaskBoard initTaskBoard = taskBoardReader.getTaskBoardById(taskBoardId).orElseThrow(
            TaskBoardNotFoundEntityException::new);

        // TaskBoardComment - HARD DELETE
        List<TaskBoardComment> commentList = initTaskBoard.getCommentList();
        for (TaskBoardComment comment : commentList) {
            taskBoardCommentStore.deleteHard(comment);
        }

        // TaskBoard - HARD DELETE
        taskBoardStore.deleteHard(initTaskBoard);

        return TaskBoardResponse.TaskBoardListDto.toDto(initTaskBoard);
    }

    /**
     * 업무 관리 게시글 서비스 - 수정 기능
     *
     * @param taskBoardId 업무 관리 게시글 ID
     * @param request 업무 관리 게시글 수정 요청 Dto
     * @return 수정을 완료한 업무 관리 게시글 Dto
     */
    @Override
    public TaskBoardItemDto update(Long taskBoardId, UpdateDto request) {

        // find TaskBoard Entity
        TaskBoard initTaskBoard = taskBoardReader.getTaskBoardById(taskBoardId).orElseThrow(
            TaskBoardNotFoundEntityException::new);

        // update
        String title = request.getTitle();
        String content = request.getContent();
        initTaskBoard.update(title, content);

        // save
        TaskBoard updatedTaskBoard = taskBoardStore.store(initTaskBoard);

        // Entity -> Dto
        return TaskBoardResponse.TaskBoardItemDto.toDto(updatedTaskBoard);
    }
}
