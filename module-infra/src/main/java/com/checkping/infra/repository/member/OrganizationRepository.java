package com.checkping.infra.repository.member;

import com.checkping.domain.member.Organization;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    // 업체명과 타입을 통한 중복 검사
    Optional<Organization> findByNameAndType(String name, Organization.Type type);

    // ID를 통한 업체 조회
    Optional<Organization> findById(Long id);

    // 타입별 업체 조회
    List<Organization> findByType(Organization.Type type);

    // 업체 전제 조회
    @Query("SELECT o FROM Organization o " +
            "WHERE (:type IS NULL OR o.type = :type) " +
            "AND (:status IS NULL OR o.status = :status)" +
            "AND (:keyword IS NULL OR (TRIM(:keyword) != '' AND LOWER(o.name) LIKE LOWER(CONCAT('%', :keyword, '%'))))")
    List<Organization> findByTypeAndStatus(
            @Param("type") Organization.Type type,
            @Param("status") Organization.Status status
//            @Param("keyword") String keyword,
//            Pageable pageable
    );

    // 상태별 업체 조회
    List<Organization> findByStatus(Organization.Status status);
}