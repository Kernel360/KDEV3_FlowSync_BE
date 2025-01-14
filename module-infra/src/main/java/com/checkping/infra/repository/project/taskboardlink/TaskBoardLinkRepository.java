package com.checkping.infra.repository.project.taskboardlink;

import com.checkping.domain.board.TaskBoardLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskBoardLinkRepository extends JpaRepository<TaskBoardLink, Long> {

}
