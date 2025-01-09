package com.checkping.service.member;

import com.checkping.dto.OrganizationRequest;
import com.checkping.dto.OrganizationResponse;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {

    void createOrganization(OrganizationRequest.CreateRequest request);

    OrganizationResponse.ReadResponse getOrganization(UUID id);

    List<OrganizationResponse.ReadResponse> getByTypeOrganizations(String type);

    List<OrganizationResponse.ReadResponse> getAllOrganizations();

    OrganizationResponse.UpdateResponse modifyOrganization(UUID id, OrganizationRequest.UpdateRequest request);
}
