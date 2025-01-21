package com.checkping.infra.repository.question;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.Question.Category;
import com.checkping.domain.question.Question.Status;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByCategory(Category category);

    List<Question> findByStatus(Status status);

    List<Question> findByTitleContaining(String taskBoardTitle);

    List<Question> findByCategoryAndStatus(Category category,
        Status status);

    List<Question> findTaskBoardByCategoryAndTitleContaining(Category category,
        String title);

    List<Question> findTaskBoardByStatusAndTitleContaining(Status status, String title);

    List<Question> findByCategoryAndStatusAndTitleContaining(Category category,
        Status status, String title);

    Optional<Question> findById(Long id);
}
