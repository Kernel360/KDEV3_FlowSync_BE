package com.checkping.infra.repository.project;

import com.checkping.domain.project.Project;
import com.checkping.infra.dto.ProjectDetailsDto;
import com.checkping.infra.dto.ProjectUpdateDetailsDto;
import com.checkping.infra.repository.project.projection.ProjectInfoProjection;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value = "SELECT p.*, " +
            "org_info.developer_type, " +
            "org_info.developer_name, " +
            "org_info.customer_type, " +
            "org_info.customer_name " +
            "FROM project p " +
            "LEFT JOIN ( " +
            "    SELECT obp.project_id, " +
            "    MAX(CASE WHEN o.type = 'DEVELOPER' THEN o.type END) AS developer_type, " +
            "    MAX(CASE WHEN o.type = 'DEVELOPER' THEN o.name END) AS developer_name, " +
            "    MAX(CASE WHEN o.type = 'CUSTOMER' THEN o.type END) AS customer_type, " +
            "    MAX(CASE WHEN o.type = 'CUSTOMER' THEN o.name END) AS customer_name " +
            "    FROM organization_by_project obp " +
            "    LEFT JOIN organization o ON obp.org_id = o.id " +
            "    GROUP BY obp.project_id " +
            ") AS org_info ON p.id = org_info.project_id " +
            "WHERE (NULLIF(:keyword, '') IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) " +
            "AND (NULLIF(:status, '') IS NULL OR p.status = :status)", nativeQuery = true)
    Page<Project> findProjectsWithOrganizationInfoByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") String status, @Param("pageable") Pageable pageable);

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
    ProjectDetailsDto findProjectById(@Param("projectId") Long projectId);

    List<ProjectInfoProjection> findByStatus(Project.Status status);
}