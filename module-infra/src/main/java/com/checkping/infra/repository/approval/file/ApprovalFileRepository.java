package com.checkping.infra.repository.approval.file;

import com.checkping.domain.approval.ApprovalFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalFileRepository extends JpaRepository<ApprovalFile, Long> {

}
