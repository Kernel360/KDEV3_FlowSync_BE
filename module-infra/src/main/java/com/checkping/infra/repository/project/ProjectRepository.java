package com.checkping.infra.repository.project;

import com.checkping.domain.project.Project;
import com.checkping.infra.dto.ProjectDetailsDto;
import com.checkping.infra.dto.ProjectListDetailsDto;
import com.checkping.infra.dto.ProjectUpdateDetailsDto;
import com.checkping.infra.repository.project.projection.ProjectInfoProjection;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value =
            "SELECT " +
            "p.id, p.name, p.description, p.detail, p.status, p.management_step, " +
            "p.reg_at, p.update_at, p.start_at, p.close_at, p.deleted_yn, p.dev_owner_id, " +
            "org_info.developer_name, org_info.customer_name, 1 AS clickable " +
            "FROM project p " +
            "LEFT JOIN ( " +
            "    SELECT obp.project_id, " +
            "    MAX(CASE WHEN o.type = 'DEVELOPER' THEN o.name END) AS developer_name, " +
            "    MAX(CASE WHEN o.type = 'CUSTOMER' THEN o.name END) AS customer_name " +
            "    FROM organization_by_project obp " +
            "    LEFT JOIN organization o ON obp.org_id = o.id " +
            "    GROUP BY obp.project_id " +
            ") AS org_info ON p.id = org_info.project_id " +
            "WHERE (NULLIF(:keyword, '') IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) " +
            "AND (NULLIF(:status, '') IS NULL OR p.status = :status)", nativeQuery = true)
    Page<ProjectListDetailsDto> findAdminProjectsByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") String status, @Param("pageable") Pageable pageable);

    @Query(value =
            "SELECT " +
            "p.id, p.name, p.description, p.detail, p.status, p.management_step, " +
            "p.reg_at, p.update_at, p.start_at, p.close_at, p.deleted_yn, p.dev_owner_id, " +
            "org_info.developer_name, org_info.customer_name, " +
            "(SELECT CASE WHEN EXISTS (SELECT 1 FROM member_by_project mbp WHERE mbp.project_id=p.id AND mbp.member_id=m.id) THEN 1 ELSE 0 END) AS clickable " +
            "FROM project p " +
            "LEFT JOIN organization_by_project obp on p.id = obp.project_id " +
            "LEFT JOIN organization o on obp.org_id = o.id " +
            "LEFT JOIN member m on o.id = m.org_id " +
            "LEFT JOIN (" +
            "    SELECT mbp.project_id, " +
            "    MAX(CASE WHEN o.type = 'DEVELOPER' THEN o.name END) AS developer_name, " +
            "    MAX(CASE WHEN o.type = 'CUSTOMER' THEN o.name END) AS customer_name " +
            "    FROM member_by_project mbp " +
            "    LEFT JOIN member m ON mbp.member_id = m.id " +
            "    LEFT JOIN organization o ON m.org_id = o.id " +
            "    GROUP BY mbp.project_id " +
            ") AS org_info ON p.id = org_info.project_id " +
            "WHERE (NULLIF(:keyword, '') IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) " +
            "AND (NULLIF(:status, '') IS NULL OR p.status = :status)" +
            "AND m.id = :memberId ", nativeQuery = true)
    Page<ProjectListDetailsDto> findDeveloperProjectsByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") String status, @Param("pageable") Pageable pageable, @Param("memberId") Long memberId);

    @Query(value =
            "SELECT " +
            "p.id, p.name, p.description, p.detail, p.status, p.management_step, " +
            "p.reg_at, p.update_at, p.start_at, p.close_at, p.deleted_yn, p.dev_owner_id, " +
            "org_info.developer_name, org_info.customer_name, 1 AS clickable " +
            "FROM project p " +
            "LEFT JOIN member_by_project mbp ON p.id = mbp.project_id " +
            "LEFT JOIN member m ON mbp.member_id = m.id " +
            "LEFT JOIN ( " +
            "    SELECT mbp.project_id, " +
            "    MAX(CASE WHEN o.type = 'DEVELOPER' THEN o.name END) AS developer_name, " +
            "    MAX(CASE WHEN o.type = 'CUSTOMER' THEN o.name END) AS customer_name " +
            "    FROM member_by_project mbp " +
            "    LEFT JOIN member m ON mbp.member_id = m.id " +
            "    LEFT JOIN organization o ON m.org_id = o.id " +
            "    GROUP BY mbp.project_id " +
            ") AS org_info ON p.id = org_info.project_id " +
            "WHERE (NULLIF(:keyword, '') IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) " +
            "AND (NULLIF(:status, '') IS NULL OR p.status = :status)" +
            "AND m.id = :memberId ", nativeQuery = true)
    Page<ProjectListDetailsDto> findCustomerProjectsByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") String status, @Param("pageable") Pageable pageable, @Param("memberId") Long memberId);


    @Query("SELECT p.managementStep, COUNT(p) AS projectCount " +
            "FROM Project p " +
            "GROUP BY p.managementStep")
    List<Tuple> countProjectsByManagementStep();

    @Query(value = "SELECT " +
            "    p.id, " +
            "    p.name AS project_name, " +
            "    p.description, " +
            "    o.name AS dev_org_name, " +
            "    m.profile_image_url, " +
            "    m.name AS member_name, " +
            "    m.job_role, " +
            "    m.job_title, " +
            "    m.phone_num, " +
            "    p.start_at, " +
            "    p.close_at " +
            "FROM project p " +
            "LEFT JOIN member m ON p.dev_owner_id = m.id " +
            "LEFT JOIN organization o ON m.org_id = o.id " +
            "WHERE p.id = :projectId", nativeQuery = true)
    Optional<ProjectDetailsDto> findProjectById(@Param("projectId") Long projectId);

    List<ProjectInfoProjection> findByStatus(Project.Status status);

    @Query(value = "SELECT p.id, p.name, p.description, p.detail, p.status, p.management_step, p.progress_step_id, p.start_at, p.close_at, p.dev_owner_id, " +
            "org_info.developer_org_id, " +
            "org_info.customer_org_id " +
            "FROM project p " +
            "LEFT JOIN (" +
            "    SELECT obp.project_id, " +
            "           MAX(CASE WHEN o.type = 'DEVELOPER' THEN o.id END) AS developer_org_id, " +
            "           MAX(CASE WHEN o.type = 'CUSTOMER' THEN o.id END) AS customer_org_id " +
            "    FROM organization_by_project obp " +
            "    LEFT JOIN organization o ON obp.org_id = o.id " +
            "    GROUP BY obp.project_id" +
            ") AS org_info " +
            "ON p.id = org_info.project_id " +
            "WHERE p.id = :projectId", nativeQuery = true)
    Optional<ProjectUpdateDetailsDto> getUpdateProjectInfoById(@Param("projectId") Long projectId);

    @Query(value = "select member_id from member_by_project where project_id= :projectId ", nativeQuery = true)
    List<Long> findProjectMemberListByProjectIdAndOrgId(Long projectId);

    boolean existsByIdAndCustomerOwnerId(Long projectId, Long customerId);
}