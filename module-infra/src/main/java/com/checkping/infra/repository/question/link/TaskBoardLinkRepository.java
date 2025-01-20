package com.checkping.infra.repository.question.link;

import com.checkping.domain.question.TaskBoardLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskBoardLinkRepository extends JpaRepository<TaskBoardLink, Long> {

}
