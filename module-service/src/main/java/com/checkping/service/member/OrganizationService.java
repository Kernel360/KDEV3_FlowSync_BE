package com.checkping.service.member;

import com.checkping.dto.OrganizationRequest;

public interface OrganizationService {

    boolean createOrganization(OrganizationRequest.OrganizationSignUpRequest request);

}
