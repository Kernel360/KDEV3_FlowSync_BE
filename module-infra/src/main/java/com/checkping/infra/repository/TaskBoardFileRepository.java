package com.checkping.infra.repository;

import com.checkping.domain.TaskBoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskBoardFileRepository extends JpaRepository<TaskBoardFile, Long> {
}
