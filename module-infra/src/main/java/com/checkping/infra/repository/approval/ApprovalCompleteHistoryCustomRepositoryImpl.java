package com.checkping.infra.repository.approval;

import static com.checkping.domain.approval.QApprovalCompleteHistory.approvalCompleteHistory;
import static java.util.Optional.ofNullable;

import com.checkping.domain.approval.ApprovalCompleteHistory;
import com.checkping.info.approval.ApprovalCompleteHistorySearchInfo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ApprovalCompleteHistoryCustomRepositoryImpl implements
    ApprovalCompleteHistoryCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ApprovalCompleteHistoryCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<ApprovalCompleteHistory> search(Long projectId, ApprovalCompleteHistorySearchInfo condition, Pageable pageable) {

        // 검색 조건
        BooleanBuilder builder = new BooleanBuilder();

        // 프로젝트 ID 필터링
        builder.and(approvalCompleteHistory.project.id.eq(projectId));

        // 프로젝트 진행 단계 필터링
        if (!Objects.isNull(condition.getProgressId())) {
            builder.and(approvalCompleteHistory.progressStep.id.eq(condition.getProgressId()));
        }

        // 총 개수 조회
        long total = ofNullable(
            queryFactory.select(approvalCompleteHistory.count()).from(approvalCompleteHistory).where(builder).fetchOne())
            .orElse(0L);

        List<ApprovalCompleteHistory> histories = queryFactory.selectFrom(approvalCompleteHistory)
            .where(builder)
            .orderBy(approvalCompleteHistory.id.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(histories, pageable, total);
    }

}
