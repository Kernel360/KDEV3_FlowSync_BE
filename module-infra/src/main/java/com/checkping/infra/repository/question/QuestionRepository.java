package com.checkping.infra.repository.question;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.Question.Status;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findById(Long id);

    Page<Question> findByProjectId(Long projectId, Pageable pageable);

    Page<Question> findByProjectIdAndProgressStepId(Long projectId, Long progressStepId, Pageable pageable);

    Page<Question> findByProjectIdAndStatus(Long projectId, Status status, Pageable pageable);

    Page<Question> findByProjectIdAndProgressStepIdAndStatus(Long projectId, Long progressId, Status status, Pageable pageable);

    Page<Question> findByProjectIdAndTitleContaining(Long projectId, String title, Pageable pageable);

    Page<Question> findByProjectIdAndProgressStepIdAndTitleContaining(Long projectId, Long progressStepId, String title, Pageable pageable);

    Page<Question> findByProjectIdAndStatusAndTitleContaining(Long projectId, Status status, String title, Pageable pageable);

    Page<Question> findByProjectIdAndProgressStepIdAndStatusAndTitleContaining(Long projectId, Long progressStepId, Status status, String title, Pageable pageable);

    Long countByProjectId(Long projectId);

    Long countByProjectIdAndProgressStepId(Long projectId, Long progressStepId);
}
