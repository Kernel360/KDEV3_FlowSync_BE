package com.checkping.infra.repository.question;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.Question.BoardCategory;
import com.checkping.domain.question.Question.BoardStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskBoardRepository extends JpaRepository<Question, Long> {

    List<Question> findByBoardCategory(BoardCategory boardCategory);

    List<Question> findByBoardStatus(BoardStatus boardStatus);

    List<Question> findByTitleContaining(String taskBoardTitle);

    List<Question> findByBoardCategoryAndBoardStatus(BoardCategory boardCategory,
        BoardStatus boardStatus);

    List<Question> findTaskBoardByBoardCategoryAndTitleContaining(BoardCategory boardCategory,
        String title);

    List<Question> findTaskBoardByBoardStatusAndTitleContaining(BoardStatus boardStatus, String title);

    List<Question> findByBoardCategoryAndBoardStatusAndTitleContaining(BoardCategory boardCategory,
        BoardStatus boardStatus, String title);

    Optional<Question> findById(Long id);
}
