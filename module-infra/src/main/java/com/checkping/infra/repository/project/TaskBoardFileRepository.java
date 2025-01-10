package com.checkping.infra.repository.project;

import com.checkping.domain.board.TaskBoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskBoardFileRepository extends JpaRepository<TaskBoardFile, Long> {
}
