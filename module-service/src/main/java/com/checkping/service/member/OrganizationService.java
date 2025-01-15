package com.checkping.service.member;

import com.checkping.dto.OrganizationCreate;
import com.checkping.dto.OrganizationGet;
import com.checkping.dto.OrganizationRequest;
import com.checkping.dto.OrganizationResponse;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {

    OrganizationCreate.Response createOrganization(OrganizationCreate.Request request);

    OrganizationGet.Response getOrganization(UUID id);

    List<OrganizationGet.Response> getByTypeAndStatusOrganizations(String type, String status);

    List<OrganizationGet.Response> getAllOrganizations(String status);

    OrganizationResponse.UpdateResponse modifyOrganization(UUID id, OrganizationRequest.UpdateRequest request);

    OrganizationResponse.ReadResponse removeOrganization(UUID id);
}