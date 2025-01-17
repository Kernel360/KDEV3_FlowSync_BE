package com.checkping.service.member;

import com.checkping.dto.*;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {

    OrganizationCreate.Response createOrganization(OrganizationCreate.Request request);

    OrganizationGet.Response getOrganization(UUID id);

    List<OrganizationGet.Response> getAllByTypeAndStatusOrganizations(String type, String status);

    OrganizationUpdate.Response modifyOrganization(UUID id, OrganizationUpdate.Request request);

    OrganizationGet.Response removeOrganization(UUID id);
}