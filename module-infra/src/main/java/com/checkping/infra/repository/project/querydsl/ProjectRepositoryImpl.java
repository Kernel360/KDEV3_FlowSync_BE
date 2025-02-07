package com.checkping.infra.repository.project.querydsl;

import com.checkping.domain.permission.QMemberByProject;
import com.checkping.domain.permission.QOrganizationByProject;
import com.checkping.domain.project.Project;

import com.checkping.domain.project.QProject;
import com.checkping.domain.project.projection.ProjectCountByManagementStep;
//import com.checkping.domain.project.projection.QProjectCount;
import com.checkping.domain.project.projection.QProjectCountByManagementStep;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProjectRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<ProjectCountByManagementStep> countProjectsByManagementStep(Long orgId, Long memberId) {
        QProject project = QProject.project;
        QOrganizationByProject obp = QOrganizationByProject.organizationByProject;
        QMemberByProject mbp = QMemberByProject.memberByProject;

        BooleanBuilder builder = new BooleanBuilder()
                .and(project.managementStep.ne(Project.ManagementStep.valueOf("DELETED")))
                .and(project.deletedYn.ne("Y"));

        JPAQuery<ProjectCountByManagementStep> query = queryFactory
                .select(new QProjectCountByManagementStep(project.managementStep.stringValue(), project.managementStep.count()))
                .from(project);

        if (orgId != null) {
            // orgId가 제공된 경우 organization_by_project 조인 추가
            query.leftJoin(obp).on(obp.id.projectId.eq(project.id));
            builder.and(obp.id.orgId.eq(orgId));
        } else if (memberId != null) {
            // memberId가 제공된 경우 member_by_project 조인 추가
            query.leftJoin(mbp).on(mbp.id.projectId.eq(project.id));
            builder.and(mbp.id.memberId.eq(memberId));
        }

        return query.where(builder).groupBy(project.managementStep).fetch();
    }
}
