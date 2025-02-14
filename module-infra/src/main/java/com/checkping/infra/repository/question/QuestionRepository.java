package com.checkping.infra.repository.question;

import com.checkping.domain.question.Question;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>,
    QuestionCustomRepository {

    Optional<Question> findById(Long id);

    @Query("SELECT q FROM Question q " +
        "LEFT JOIN FETCH q.commentList c " +
        "WHERE q.id = :questionId " +
        "ORDER BY COALESCE(c.parent.id, c.id) , c.regAt ASC")
    Optional<Question> findByIdWithComments(@Param("questionId") Long id);

    Long countByProjectId(Long projectId);

    Long countByProjectIdAndProgressStepId(Long projectId, Long progressStepId);

    boolean existsByProjectIdAndId(Long projectId, Long id);
}
