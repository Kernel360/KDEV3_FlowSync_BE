package com.checkping.infra.repository.question.link;

import com.checkping.domain.question.QuestionLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionLinkRepository extends JpaRepository<QuestionLink, Long> {

}
