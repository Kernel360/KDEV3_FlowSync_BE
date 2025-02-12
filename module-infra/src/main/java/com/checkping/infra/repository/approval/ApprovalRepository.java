package com.checkping.infra.repository.approval;

import com.checkping.domain.approval.Approval;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long>, ApprovalCustomRepository {

    @Query("SELECT a FROM Approval a "
        + "LEFT JOIN FETCH a.commentList c "
        + "WHERE a.id = :approvalId "
        + "ORDER BY COALESCE(c.parent.id, c.id), c.regAt ASC")
    Optional<Approval> getByIdWithComments(@Param("approvalId") Long approvalId);

    boolean existsByProjectIdAndId(Long projectId, Long approvalId);

    boolean existsByIdAndRegisterId(Long approvalId, Long registerId);
}
