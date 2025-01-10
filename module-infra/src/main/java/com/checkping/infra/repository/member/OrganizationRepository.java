package com.checkping.infra.repository.member;

import com.checkping.domain.member.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    // 업체명과 타입을 통한 중복 검사
    Optional<Organization> findByNameAndType(String name, Organization.Type type);

    // ID를 통한 업체 조회
    Optional<Organization> findById(UUID id);

    // 타입별 업체 조회
    List<Organization> findByType(Organization.Type type);

    // 타입별 상태별 업체 조회 -
    List<Organization> findByTypeAndStatus(Organization.Type type, Organization.Status status);

    // 상태별 업체 조회
    List<Organization> findByStatus(Organization.Status status);

}