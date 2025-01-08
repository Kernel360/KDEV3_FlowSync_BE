package com.checkping.infra.repository.member;

import com.checkping.domain.member.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

    Optional<Organization> findByNameAndType(String name, Organization.Type type);

    Optional<Organization> findById(UUID id);

    List<Organization> findByType(Organization.Type type);

}