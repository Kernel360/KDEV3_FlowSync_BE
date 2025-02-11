package com.checkping.infra.repository.member;

import com.checkping.domain.member.Organization;
import com.checkping.domain.member.QOrganization;
import com.checkping.domain.member.projection.QProjectListGet;
import com.checkping.domain.permission.QMemberByProject;
import com.checkping.domain.permission.QOrganizationByProject;
import com.checkping.domain.project.QProject;
import com.checkping.domain.member.projection.ProjectListGet;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class MemberOrganizationQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<ProjectListGet> getProjectsByMemberAndOrganization(Long organizationId, Long memberId) {

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

        // 멤버 ID가 전달되었으면 (즉, 일반 회원이면) 본인이 속한 프로젝트만 조회
        if (memberId != null) {
            builder.and(p.id.in(
                    JPAExpressions.select(mbp.id.projectId)
                            .from(mbp)
                            .where(mbp.id.memberId.eq(memberId))
            ));
        }

        return query.fetch();

    }


}
