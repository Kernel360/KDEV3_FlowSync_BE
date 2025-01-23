package com.checkping.service.member;

import com.checkping.common.dto.PageInfo;
import com.checkping.dto.OrganizationCreate;
import com.checkping.dto.OrganizationGet;
import com.checkping.dto.OrganizationUpdate;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface OrganizationService {

    OrganizationCreate.Response createOrganization(OrganizationCreate.Request request, MultipartFile file);

    OrganizationGet.Response getOrganization(UUID id);

    PageInfo.Response<OrganizationGet.Response> getListOrganization(String type, String status, PageInfo.Request pageRequest);

    OrganizationUpdate.Response modifyOrganization(UUID id, OrganizationUpdate.Request request, MultipartFile file);

    OrganizationGet.Response removeOrganization(UUID id);
}