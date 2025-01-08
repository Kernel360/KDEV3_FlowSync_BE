package com.checkping.infra.repository.member;

import com.checkping.domain.member.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

}
