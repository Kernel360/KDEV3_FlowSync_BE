package com.checkping.service.member;

import com.checkping.dto.OrganizationRequest;
import com.checkping.dto.OrganizationResponse;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {

    void createOrganization(OrganizationRequest.OrganizationCreateRequest request);

    OrganizationResponse.OrganizationReadResponse getOrganization(UUID id);

    List<OrganizationResponse.OrganizationReadResponse> getByTypeOrganizations(String type);

    List<OrganizationResponse.OrganizationReadResponse> getAllOrganizations();

}
