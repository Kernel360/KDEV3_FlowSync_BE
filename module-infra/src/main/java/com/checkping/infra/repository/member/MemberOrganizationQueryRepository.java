package com.checkping.infra.repository.member;

import com.checkping.domain.member.Organization;
import com.checkping.domain.member.QOrganization;
import com.checkping.domain.member.projection.ProjectListGet;
import com.checkping.domain.member.projection.QProjectListGet;
import com.checkping.domain.permission.QMemberByProject;
import com.checkping.domain.permission.QOrganizationByProject;
import com.checkping.domain.project.Project;
import com.checkping.domain.project.QProject;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MemberOrganizationQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<ProjectListGet> getProjectsByMemberAndOrganization(Long organizationId, Long memberId, Project.ManagementStep managementStep, String keyword, Pageable pageable) {

        QProject p = QProject.project;
        QOrganizationByProject obp = QOrganizationByProject.organizationByProject;
        QOrganizationByProject obpDev = new QOrganizationByProject("obpDev");
        QOrganizationByProject obpCust = new QOrganizationByProject("obpCust");
        QOrganization devOrg = new QOrganization("devOrg");
        QOrganization custOrg = new QOrganization("custOrg");
        QMemberByProject mbp = QMemberByProject.memberByProject;

        BooleanBuilder builder = new BooleanBuilder();

        JPAQuery<ProjectListGet> query = jpaQueryFactory
                .selectDistinct(new QProjectListGet(
                                p.id,
                                p.name,
                                devOrg.name,
                                custOrg.name,
                                p.managementStep,
                                p.startAt,
                                p.closeAt,
                                p.updateAt
                        )
                )
                .from(p)

                // 개발사 조인
                .join(obpDev).on(p.id.eq(obpDev.id.projectId))
                .join(devOrg).on(obpDev.id.orgId.eq(devOrg.id).and(devOrg.type.eq(Organization.Type.DEVELOPER)))

                // 고객사 조인
                .join(obpCust).on(p.id.eq(obpCust.id.projectId))
                .join(custOrg).on(obpCust.id.orgId.eq(custOrg.id).and(custOrg.type.eq(Organization.Type.CUSTOMER)));

        // 업체 ID가 있을 경우 → 특정 업체가 속한 프로젝트만 필터링
        if (organizationId != null) {
            query.join(obp).on(p.id.eq(obp.id.projectId));
            builder.and(obp.id.orgId.eq(organizationId));
        }

        // 멤버 ID가 있을 경우 → 본인이 속한 프로젝트만 조회
        if (memberId != null) {
            builder.and(p.id.in(
                    JPAExpressions.select(mbp.id.projectId)
                            .from(mbp)
                            .where(mbp.id.memberId.eq(memberId))
            ));
        }

        // 관리 단계 필터링 (`managementStep`이 있으면 필터링)
        if (managementStep != null) {
            builder.and(p.managementStep.eq(managementStep));
        }

        // 키워드 검색 (프로젝트명에서만 검색, 빈 문자열 체크 추가)
        if (keyword != null && !keyword.trim().isEmpty()) {
            builder.and(p.name.containsIgnoreCase(keyword));
        }

        query.where(builder);

        // 페이징 처리 개선
        List<ProjectListGet> results = query
                .offset(pageable.getOffset())  // 시작 위치 설정
                .limit(pageable.getPageSize())  // 페이지 크기 설정
                .fetch();

        long totalCount = query.fetchCount();

        return new PageImpl<>(results, pageable, totalCount);
    }


}
