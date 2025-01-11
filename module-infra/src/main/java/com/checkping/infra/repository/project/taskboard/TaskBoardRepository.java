package com.checkping.infra.repository.project.taskboard;

import com.checkping.domain.board.TaskBoard;
import com.checkping.domain.board.TaskBoard.BoardCategory;
import com.checkping.domain.board.TaskBoard.BoardStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskBoardRepository extends JpaRepository<TaskBoard, Long> {

    List<TaskBoard> findByBoardCategory(BoardCategory boardCategory);

    List<TaskBoard> findByBoardStatus(BoardStatus boardStatus);

    List<TaskBoard> findByBoardCategoryAndBoardStatus(BoardCategory boardCategory,
        BoardStatus boardStatus);

    Optional<TaskBoard> findById(Long id);
}
