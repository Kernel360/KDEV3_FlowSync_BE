package com.checkping.infra.repository.project.taskboard;

import com.checkping.domain.board.TaskBoard;
import com.checkping.domain.board.TaskBoard.BoardCategory;
import com.checkping.domain.board.TaskBoard.BoardStatus;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskBoardReaderImpl implements TaskBoardReader {

    private final TaskBoardRepository taskBoardRepository;

    /**
     * TaskBoard 전체 조회 및 필터링 조회
     *
     * @param boardCategory TaskBoard.BoardCategory
     * @param boardStatus TaskBoard.BoardStatus
     * @return TaskBoard 전체 조회
     */
    @Override
    public List<TaskBoard> getTaskBoard(BoardCategory boardCategory, BoardStatus boardStatus) {

        // boardCategory AND boardStatus
        if (boardCategory != null && boardStatus != null) {
            return taskBoardRepository.findByBoardCategoryAndBoardStatus(boardCategory, boardStatus);
        }

        // boardCategory
        if (boardCategory != null) {
            return taskBoardRepository.findByBoardCategory(boardCategory);
        }

        // boardStatus
        if (boardStatus != null) {
            return taskBoardRepository.findByBoardStatus(boardStatus);
        }

        // 조회
        return taskBoardRepository.findAll();
    }

    /**
     * TaskBoard 조회 기능
     *
     * @param id taskBoard 아이디
     * @return TaskBoard 조회 결과
     */
    @Override
    public Optional<TaskBoard> getTaskBoardById(Long id) {
        return taskBoardRepository.findById(id);
    }
}
