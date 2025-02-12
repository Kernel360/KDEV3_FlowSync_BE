package com.checkping.infra.repository.approval;

import static com.checkping.domain.approval.QApproval.approval;
import static com.checkping.domain.approval.QApprovalComment.approvalComment;
import static com.checkping.domain.project.QProgressStep.progressStep;
import static java.util.Optional.ofNullable;

import com.checkping.domain.approval.Approval;
import com.checkping.domain.approval.ApprovalComment;
import com.checkping.info.approval.ApprovalCountProjection;
import com.checkping.info.approval.ApprovalSearchInfo.SearchCondition;
import com.checkping.info.approval.QApprovalCountProjection;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class ApprovalCustomRepositoryImpl implements ApprovalCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ApprovalCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<ApprovalCountProjection> countByProgressStep(Long projectId) {

        JPAQuery<ApprovalCountProjection> query = queryFactory.select(
                new QApprovalCountProjection(progressStep.id,                     // ✅ 진행 단계 ID
                    progressStep.name,                   // ✅ 진행 단계 이름
                    progressStep.description,            // ✅ 진행 단계 설명
                    approval.count().coalesce(0L),       // ✅ 개수가 없으면 0 반환
                    progressStep.status.stringValue(), // ✅ 진행 단계 상태
                    progressStep.stepOrder                // ✅ 진행 단계 순서
                )).from(progressStep) // ✅ 진행 단계 테이블을 기준으로 조회
            .leftJoin(approval).on(approval.progressStep.id.eq(progressStep.id)
                .and(approval.project.id.eq(projectId)) // 특정 프로젝트 내에서만 조회
                .and(approval.deleteYn.eq(Approval.DeleteStatus.N)) // 삭제되지 않은 데이터만 포함
            ).where(progressStep.projectId.eq(projectId) // ✅ 프로젝트 ID 조건
            ).groupBy(progressStep.id); // ✅ 진행 단계 ID로 그룹화

        return query.fetch();
    }

    @Override
    public Page<Approval> getByCondition(Long projectId, SearchCondition searchCondition,
        Pageable pageable) {

        // ✅ 동적 검색 조건을 위한 BooleanBuilder
        BooleanBuilder builder = new BooleanBuilder();

        // ✅ 필수 조건: 프로젝트 ID
        builder.and(approval.project.id.eq(projectId));

        // ✅ 삭제상태 조건: 삭제되지 않은 데이터만 조회 (
        if (!searchCondition.adminSearch()) {
            builder.and(approval.deleteYn.eq(Approval.DeleteStatus.N));
        }

        // ✅ 검색어 조건 (title에 포함된 검색어)
        if (StringUtils.hasText(searchCondition.keyword())) {
            builder.and(approval.title.containsIgnoreCase(searchCondition.keyword()));
        }

        // ✅ 상태 조건
        if (searchCondition.status() != null) {
            builder.and(approval.status.eq(searchCondition.status()));
        }

        // ✅ 진행 단계 조건
        if (searchCondition.progressId() != null) {
            builder.and(approval.progressStep.id.eq(searchCondition.progressId()));
        }

        // ✅ 총 개수 조회
        long total = ofNullable(
            queryFactory.select(approval.count()).from(approval).where(builder).fetchOne())
            .orElse(0L);

        // ✅ 페이징된 데이터 조회
        List<Approval> approvals = queryFactory.selectFrom(approval).where(builder)
            .offset(pageable.getOffset()) // ✅ 페이징 처리 (시작 위치)
            .limit(pageable.getPageSize()) // ✅ 페이지 크기 지정
            .fetch();

        return new PageImpl<>(approvals, pageable, total);
    }

    @Override
    public Optional<Approval> getApprovalWithComment(Long projectId, Long approvalId,
        boolean isDeleted) {

        // ✅ 동적 검색 조건을 위한 BooleanBuilder
        BooleanBuilder builder = new BooleanBuilder();

        // ✅ 필수 조건: 프로젝트 ID
        builder.and(approval.project.id.eq(projectId));

        // ✅ 필수 조건: 결재 ID
        builder.and(approval.id.eq(approvalId));

        // ✅ 삭제상태 조건: 삭제된 데이터도 조회
        if (!isDeleted) {
            builder.and(approval.deleteYn.eq(Approval.DeleteStatus.N));
            // 댓글이 없거나 삭제되지 않은 상태만 조회
            builder.and(
                approvalComment.isNull()
                    .or(approvalComment.deleteYn.eq(
                        ApprovalComment.DeleteStatus.N))); // ✅ 삭제된 데이터는 조회하지 않음
        }

        // ✅ 결재 데이터 조회
        Approval result = queryFactory
            .selectFrom(approval)
            .leftJoin(approval.commentList, approvalComment)
            .where(builder)
            .fetchOne();

        return Optional.ofNullable(result); // ✅ 결과를 Optional로 감싸서 반환
    }
}
