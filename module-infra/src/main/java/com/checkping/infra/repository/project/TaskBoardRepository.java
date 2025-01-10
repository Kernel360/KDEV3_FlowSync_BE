package com.checkping.infra.repository.project;

import com.checkping.domain.board.TaskBoard;
import com.checkping.domain.board.TaskBoard.BoardCategory;
import com.checkping.domain.board.TaskBoard.BoardStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskBoardRepository extends JpaRepository<TaskBoard, Long> {

    List<TaskBoard> findByBoardCategory(BoardCategory boardCategory);

    List<TaskBoard> findByBoardStatus(BoardStatus boardStatus);

    List<TaskBoard> findByBoardCategoryAndBoardStatus(BoardCategory boardCategory,
        BoardStatus boardStatus);
}
