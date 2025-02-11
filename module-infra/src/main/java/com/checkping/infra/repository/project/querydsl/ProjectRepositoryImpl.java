package com.checkping.infra.repository.project.querydsl;

import com.checkping.domain.member.QMember;
import com.checkping.domain.member.QOrganization;
import com.checkping.domain.permission.QMemberByProject;
import com.checkping.domain.permission.QOrganizationByProject;
import com.checkping.domain.project.Project;

import com.checkping.domain.project.QProject;
import com.checkping.domain.project.projection.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Page<ProjectListInfoByManagementStep> findProjectsByManagementSteps(Long orgId, Long memberId, String manageStep, Pageable pageable) {
        QProject project = QProject.project;
        QMemberByProject mbp = QMemberByProject.memberByProject;
        QOrganizationByProject obp = QOrganizationByProject.organizationByProject;

        BooleanBuilder builder = new BooleanBuilder()
                .and(project.managementStep.eq(Project.ManagementStep.valueOf(manageStep)))
                .and(project.deletedYn.eq("N"));

        JPAQuery<ProjectListInfoByManagementStep> query = queryFactory
                .select(new QProjectListInfoByManagementStep(project.id, project.name))
                .from(project);

        if (orgId != null) {
            query.leftJoin(obp).on(obp.id.projectId.eq(project.id));
            builder.and(obp.id.orgId.eq(orgId));
        } else if (memberId != null) {
            query.leftJoin(mbp).on(mbp.id.projectId.eq(project.id));
            builder.and(mbp.id.memberId.eq(memberId));
        }

        query.where(builder)
                .orderBy(project.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        QueryResults<ProjectListInfoByManagementStep> results = query.fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    public List<Long> memberByProject(Long memberId, Pageable pageable) {
        QProject project = QProject.project;
        QMemberByProject mbp = QMemberByProject.memberByProject;

        JPAQuery<Long> query = queryFactory
                .select(project.id)
                .from(project)
                .leftJoin(mbp).on(mbp.id.projectId.eq(project.id))
                .where(project.managementStep.eq(Project.ManagementStep.valueOf("IN_PROGRESS"))
                        .and(project.deletedYn.eq("N").and(mbp.id.memberId.eq(memberId))))
                .orderBy(project.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        return query.fetch();
    }

    @Override
    public Optional<ProjectInfo> findProjectInfoById(Long projectId) {
        QProject project = QProject.project;

        ProjectInfo projectInfo = queryFactory
                .select(Projections.constructor(ProjectInfo.class,
                        project.id,
                        project.name.as("projectName"),
                        project.description,
                        project.managementStep,
                        project.devOwner.id.as("developerOwnerId"),
                        project.customerOwner.id.as("customerOwnerId"),
                        project.startAt,
                        project.closeAt))
                .from(project)
                .where(project.id.eq(projectId))
                .fetchOne();

        return Optional.ofNullable(projectInfo);
    }

    public Optional<OwnerInfo> findOwnerMemberInfoById(Long memberId) {
        QMember member = QMember.member;
        QOrganization organization = QOrganization.organization;

        OwnerInfo ownerInfo = queryFactory
                .select(Projections.constructor(OwnerInfo.class,
                        organization.name.as("ownerOrgName"),
                        member.name.as("ownerName"),
                        member.profileImageUrl,
                        member.jobRole,
                        member.jobTitle,
                        member.phoneNum))
                .from(member)
                .leftJoin(organization).on(member.organization.id.eq(organization.id))
                .where(member.id.eq(memberId))
                .fetchOne();

        return Optional.ofNullable(ownerInfo);
    }

}
