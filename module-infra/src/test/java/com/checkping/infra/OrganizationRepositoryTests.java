package com.checkping.infra;

import com.checkping.domain.member.Organization;
import com.checkping.infra.repository.member.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class OrganizationRepositoryTests {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    void testCreateAndGetAllOrganization(){
        for(int i = 0; i<5; i++) {
            Organization organization = Organization.builder()
                    .name("kernel")
                    .type(Organization.Type.CUSTOMER)
                    .status(Organization.Status.ACTIVE)
                    .regAt(LocalDateTime.now())
                    .build();

            organizationRepository.save(organization);
        }
        List<Organization> organizations = organizationRepository.findAll();
        System.out.println(organizations);
    }

}
