package com.checkping.infra.repository.question.comment;

import com.checkping.domain.question.QuestionComment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskBoardCommentRepository extends JpaRepository<QuestionComment, Long> {
    Optional<QuestionComment> findById(Long id);
    boolean existsByIdAndQuestionId(Long id, Long questionId);
}
