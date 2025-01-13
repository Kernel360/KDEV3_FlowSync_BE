package com.checkping.infra.repository.project;

import com.checkping.domain.project.Project;
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
            "WHERE (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%')) " +
            "AND (:status IS NULL OR p.status = :status)", nativeQuery = true)
    List<Project> findProjectsWithOrganizationInfoByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") String status);


}