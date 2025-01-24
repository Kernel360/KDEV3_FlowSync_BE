package com.checkping.infra.repository.question;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.Question.Category;
import com.checkping.domain.question.Question.Status;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByCategory(Category category);

    List<Question> findByStatus(Status status);

    List<Question> findByTitleContaining(String taskBoardTitle);

    List<Question> findByCategoryAndStatus(Category category,
        Status status);

    List<Question> findQuestionByCategoryAndTitleContaining(Category category,
        String title);

    List<Question> findQuestionByStatusAndTitleContaining(Status status, String title);

    List<Question> findByCategoryAndStatusAndTitleContaining(Category category,
        Status status, String title);

    Optional<Question> findById(Long id);

    Page<Question> findByProjectId(Long projectId, Pageable pageable);

    Page<Question> findByProjectIdAndCategory(Long projectId, Category category, Pageable pageable);

    Page<Question> findByProjectIdAndStatus(Long projectId, Status status, Pageable pageable);

    Page<Question> findByProjectIdAndCategoryAndStatus(Long projectId, Category category, Status status, Pageable pageable);

    Page<Question> findByProjectIdAndTitleContaining(Long projectId, String title, Pageable pageable);

    Page<Question> findByProjectIdAndCategoryAndTitleContaining(Long projectId, Category category, String title, Pageable pageable);

    Page<Question> findByProjectIdAndStatusAndTitleContaining(Long projectId, Status status, String title, Pageable pageable);

    Page<Question> findByProjectIdAndCategoryAndStatusAndTitleContaining(Long projectId, Category category, Status status, String title, Pageable pageable);
}
