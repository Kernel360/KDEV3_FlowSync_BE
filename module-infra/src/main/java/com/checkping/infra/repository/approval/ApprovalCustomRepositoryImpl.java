package com.checkping.infra.repository.approval;

import static com.checkping.domain.approval.QApproval.approval;
import static com.checkping.domain.project.QProgressStep.progressStep;

import com.checkping.domain.approval.Approval;
import com.checkping.info.approval.ApprovalCountProjection;
import com.checkping.info.approval.QApprovalCountProjection;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ApprovalCustomRepositoryImpl implements ApprovalCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ApprovalCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<ApprovalCountProjection> countByProgressStep(Long projectId) {

        JPAQuery<ApprovalCountProjection> query = queryFactory
            .select(new QApprovalCountProjection(
                progressStep.id,                     // ✅ 진행 단계 ID
                progressStep.name,                   // ✅ 진행 단계 이름
                progressStep.description,            // ✅ 진행 단계 설명
                approval.count().coalesce(0L),       // ✅ 개수가 없으면 0 반환
                progressStep.status.stringValue() // ✅ 진행 단계 상태
            ))
            .from(progressStep) // ✅ 진행 단계 테이블을 기준으로 조회
            .leftJoin(approval).on(
                approval.progressStep.id.eq(progressStep.id)
                    .and(approval.project.id.eq(projectId)) // 특정 프로젝트 내에서만 조회
                    .and(approval.deleteYn.eq(Approval.DeleteStatus.N)) // 삭제되지 않은 데이터만 포함
            )
            .where(
                progressStep.projectId.eq(projectId) // ✅ 프로젝트 ID 조건
            )
            .groupBy(progressStep.id); // ✅ 진행 단계 ID로 그룹화

        return query.fetch();
    }
}
