package com.checkping.service;

import com.checkping.dto.OrganizationRequest;
import com.checkping.service.member.OrganizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrganizationServiceTests {

    @Autowired
    private OrganizationService organizationService;

    @Test
    void testCreateOrganization() {

        organizationService.createOrganization(OrganizationRequest.OrganizationSignUpRequest.builder()
                .type("CUSTOMER")
                .brNumber("123456")
                .name("커널고객사")
                .brCertificateUrl("awge76876w-asdg7djh")
                .streetAddress("강남대로 364")
                .detailAddress("")
                .phoneNumber("")
                .build());
    }

}
