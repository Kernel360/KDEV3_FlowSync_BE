package com.checkping.service;

import com.checkping.domain.member.Organization;
import com.checkping.dto.OrganizationRequest;
import com.checkping.dto.OrganizationResponse;
import com.checkping.infra.repository.member.OrganizationRepository;
import com.checkping.service.member.OrganizationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class OrganizationServiceTests {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @BeforeAll
    static void init(@Autowired OrganizationService organizationService) {
        for (int i = 0; i < 10; i++) {
            organizationService.createOrganization(OrganizationRequest.OrganizationCreateRequest.builder()
                    .type(i < 5 ? "CUSTOMER" : "DEVELOPER")
                    .brNumber("123456" + i)
                    .name("커널" + i)
                    .brCertificateUrl("cert-url-")
                    .streetAddress("강남대로")
                    .detailAddress("테스트입니다")
                    .phoneNumber("010-1111-1111")
                    .build());
        }

    }

    @Test
    void testCreateOrganization() {
        organizationService.createOrganization(OrganizationRequest.OrganizationCreateRequest.builder()
                .type("CUSTOMER")
                .brNumber("123456mnb")
                .name("새로운 커널고객사")
                .brCertificateUrl("awge76876w-asdg7djh")
                .streetAddress("강남대로 364")
                .detailAddress("")
                .phoneNumber("")
                .build());
    }

    @Test
    void testGetOrganizationById() {
        Organization organization =
                organizationRepository.save(OrganizationRequest.OrganizationCreateRequest.toEntity(OrganizationRequest.OrganizationCreateRequest.builder()
                        .type("CUSTOMER")
                        .brNumber("123456mnb")
                        .name("새로운 커널")
                        .brCertificateUrl("awge76876w-asdg7djh")
                        .streetAddress("강남대로 364")
                        .detailAddress("")
                        .phoneNumber("")
                        .build()));

        OrganizationResponse.OrganizationReadResponse organizationResponse =
                organizationService.getOrganization(organization.getId());

    }

    @Test
    void testGetByTypeOrganization() {
        List<OrganizationResponse.OrganizationReadResponse> organizationCsList =
                organizationService.getByTypeOrganizations("CUSTOMER");

        List<OrganizationResponse.OrganizationReadResponse> organizationDevList =
                organizationService.getByTypeOrganizations("DEVELOPER");

    }

    @Test
    void testGetAllOrganizations() {
        List<OrganizationResponse.OrganizationReadResponse> organizations =
                organizationService.getAllOrganizations();

    }


    @Test
    void testModifyOrganization() {
        Organization organization =
                organizationRepository.save(OrganizationRequest.OrganizationCreateRequest.toEntity(OrganizationRequest.OrganizationCreateRequest.builder()
                        .type("CUSTOMER")
                        .brNumber("1234567890")
                        .name("커널커널")
                        .brCertificateUrl("awge76876w-asdg7djh")
                        .streetAddress("강남대로 364")
                        .detailAddress("")
                        .phoneNumber("")
                        .build()));

        OrganizationRequest.OrganizationUpdateRequest request = OrganizationRequest.OrganizationUpdateRequest.builder()
                .brNumber("123456789")
                .brCertificateUrl("cert-url-asdfasdf")
                .streetAddress("안녕하세요")
                .detailAddress("안녕하십니까")
                .phoneNumber("010-1111-1112")
                .build();

        OrganizationResponse.OrganizationUpdateResponse response = organizationService.modifyOrganization(
                organization.getId(),
                request
        );
    }
}