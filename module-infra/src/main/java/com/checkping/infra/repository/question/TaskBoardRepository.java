package com.checkping.infra.repository.question;

import com.checkping.domain.question.TaskBoard;
import com.checkping.domain.question.TaskBoard.BoardCategory;
import com.checkping.domain.question.TaskBoard.BoardStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskBoardRepository extends JpaRepository<TaskBoard, Long> {

    List<TaskBoard> findByBoardCategory(BoardCategory boardCategory);

    List<TaskBoard> findByBoardStatus(BoardStatus boardStatus);

    List<TaskBoard> findByTitleContaining(String taskBoardTitle);

    List<TaskBoard> findByBoardCategoryAndBoardStatus(BoardCategory boardCategory,
        BoardStatus boardStatus);

    List<TaskBoard> findTaskBoardByBoardCategoryAndTitleContaining(BoardCategory boardCategory,
        String title);

    List<TaskBoard> findTaskBoardByBoardStatusAndTitleContaining(BoardStatus boardStatus, String title);

    List<TaskBoard> findByBoardCategoryAndBoardStatusAndTitleContaining(BoardCategory boardCategory,
        BoardStatus boardStatus, String title);

    Optional<TaskBoard> findById(Long id);
}
