package com.checkping.infra.repository.project.taskboardcomment;

import com.checkping.domain.board.TaskBoardComment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskBoardCommentRepository extends JpaRepository<TaskBoardComment, Long> {
    Optional<TaskBoardComment> findById(Long id);
    boolean existsByIdAndTaskBoardId(Long id, Long taskBoardId);
}
