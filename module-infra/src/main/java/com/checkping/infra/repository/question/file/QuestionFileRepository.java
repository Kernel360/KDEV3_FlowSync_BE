package com.checkping.infra.repository.question.file;

import com.checkping.domain.question.QuestionFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionFileRepository extends JpaRepository<QuestionFile, Long> {
}
