package com.checkping.service.member;

import com.checkping.dto.OrganizationRequest;

public interface OrganizationService {

    void createOrganization(OrganizationRequest.OrganizationSignUpRequest request);

}
