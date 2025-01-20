package com.checkping.infra.repository.question.comment;

import com.checkping.domain.question.TaskBoardComment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskBoardCommentRepository extends JpaRepository<TaskBoardComment, Long> {
    Optional<TaskBoardComment> findById(Long id);
    boolean existsByIdAndTaskBoardId(Long id, Long taskBoardId);
}
