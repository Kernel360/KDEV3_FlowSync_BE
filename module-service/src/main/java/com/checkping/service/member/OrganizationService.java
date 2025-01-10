package com.checkping.service.member;

import com.checkping.dto.OrganizationRequest;
import com.checkping.dto.OrganizationResponse;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {

    OrganizationResponse.ReadResponse createOrganization(OrganizationRequest.CreateRequest request);

    OrganizationResponse.ReadResponse getOrganization(UUID id);

    List<OrganizationResponse.ReadResponse> getByTypeAndStatusOrganizations(String type, String status);

    List<OrganizationResponse.ReadResponse> getAllOrganizations(String status);

}
