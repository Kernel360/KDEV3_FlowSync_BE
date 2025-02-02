package com.checkping.infra.repository.approval.link;

import com.checkping.domain.approval.ApprovalLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalLinkRepository extends JpaRepository<ApprovalLink, Long> {

}
