package com.checkping.infra;

import com.checkping.domain.member.Organization;
import com.checkping.infra.repository.member.OrganizationRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Log4j2
class OrganizationRepositoryTests {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    void testCreateOrganization() {
        for (int i = 0; i < 5; i++) {
            Organization organization = Organization.builder()
                    .name("kernel")
                    .type(Organization.Type.CUSTOMER)
                    .status(Organization.Status.ACTIVE)
                    .regAt(LocalDateTime.now())
                    .build();

            Organization savedOrganization = organizationRepository.save(organization);
            Assertions.assertNotNull(savedOrganization.getId());
        }
    }

    @Test
    void testCreateAndGetOrganization() {

        UUID id = null;

        for (int i = 0; i < 5; i++) {
            Organization organization = Organization.builder()
                    .name("kernel")
                    .type(Organization.Type.CUSTOMER)
                    .status(Organization.Status.ACTIVE)
                    .regAt(LocalDateTime.now())
                    .build();

            organizationRepository.save(organization);
            if (i == 4) {
                id = organization.getId();
            }
        }
        Optional<Organization> result = organizationRepository.findById(id);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    void testCreateAndGetByTypeOrganization() {
        for (int i = 0; i < 10; i++) {
            Organization organization;
            if (i < 5) {
                organization = Organization.builder()
                        .name("kernel")
                        .type(Organization.Type.CUSTOMER)
                        .status(Organization.Status.ACTIVE)
                        .regAt(LocalDateTime.now())
                        .build();
            } else {
                organization = Organization.builder()
                        .name("kernel")
                        .type(Organization.Type.DEVELOPER)
                        .status(Organization.Status.ACTIVE)
                        .regAt(LocalDateTime.now())
                        .build();
            }
            organizationRepository.save(organization);
        }
        List<Organization> organizationsDev = organizationRepository.findByType(Organization.Type.DEVELOPER);
        List<Organization> organizationsCs = organizationRepository.findByType(Organization.Type.CUSTOMER);

        Assertions.assertNotNull(organizationsDev);
        Assertions.assertNotNull(organizationsCs);
    }

    @Test
    void testCreateAndGetAllOrganization() {
        for (int i = 0; i < 10; i++) {
            Organization organization;
            if (i < 5) {
                organization = Organization.builder()
                        .name("kernel")
                        .type(Organization.Type.CUSTOMER)
                        .status(Organization.Status.ACTIVE)
                        .regAt(LocalDateTime.now())
                        .build();
            } else {
                organization = Organization.builder()
                        .name("kernel")
                        .type(Organization.Type.DEVELOPER)
                        .status(Organization.Status.ACTIVE)
                        .regAt(LocalDateTime.now())
                        .build();
            }
            organizationRepository.save(organization);
        }
        List<Organization> organizations = organizationRepository.findAll();

        Assertions.assertNotNull(organizations);
    }

}