package com.checkping.service.member;

import com.checkping.common.dto.PageInfo;
import com.checkping.dto.OrganizationCreate;
import com.checkping.dto.OrganizationDelete;
import com.checkping.dto.OrganizationListGet;
import com.checkping.dto.OrganizationUpdate;
import org.springframework.web.multipart.MultipartFile;

public interface OrganizationService {

    OrganizationCreate.Response createOrganization(OrganizationCreate.Request request, MultipartFile file);

    OrganizationListGet.Response getOrganization(Long id);

    PageInfo.Response<OrganizationListGet.Response> getListOrganization(String type, String status, PageInfo.Request pageRequest);

    OrganizationUpdate.Response modifyOrganization(Long id, OrganizationUpdate.Request request, MultipartFile file);

    OrganizationDelete.Response removeOrganization(Long id, OrganizationDelete.Request request);

    OrganizationDelete.Response changeStatusOrganization(Long id, OrganizationDelete.Request request);
}