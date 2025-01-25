package com.checkping.service.member;

import com.checkping.dto.OrganizationCreate;
import com.checkping.dto.OrganizationGet;
import com.checkping.dto.OrganizationUpdate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OrganizationService {

    OrganizationCreate.Response createOrganization(OrganizationCreate.Request request, MultipartFile file);

    OrganizationGet.Response getOrganization(Long id);

    List<OrganizationGet.Response> getAllByTypeAndStatusOrganizations(String type, String status);

    OrganizationUpdate.Response modifyOrganization(Long id, OrganizationUpdate.Request request, MultipartFile file);

    OrganizationGet.Response removeOrganization(Long id);
}