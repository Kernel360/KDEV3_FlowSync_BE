package com.checkping.infra.repository.approval;

import static com.checkping.domain.approval.QApproval.approval;

import com.checkping.domain.approval.Approval;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ApprovalCustomRepositoryImpl implements ApprovalCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ApprovalCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Long countByProgressStep(Long projectId, Long progressStepId) {
        return queryFactory
            .select(approval.count())
            .from(approval)
            .where(
                approval.project.id.eq(projectId), // 프로젝트 ID
                approval.progressStep.id.eq(progressStepId), // 진행상태 ID
                approval.deleteYn.eq(Approval.DeleteStatus.N) // 삭제 여부
            )
            .fetchOne();
    }
}
