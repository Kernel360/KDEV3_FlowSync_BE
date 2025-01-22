package com.checkping.service.member;

import com.checkping.common.dto.PageRequestDto;
import com.checkping.common.dto.PageResponseDto;
import com.checkping.dto.OrganizationCreate;
import com.checkping.dto.OrganizationGet;
import com.checkping.dto.OrganizationUpdate;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface OrganizationService {

    OrganizationCreate.Response createOrganization(OrganizationCreate.Request request, MultipartFile file);

    OrganizationGet.Response getOrganization(UUID id);

    PageResponseDto<OrganizationGet.Response> getListOrganization(String type, String status, PageRequestDto pageRequestDto);

    OrganizationUpdate.Response modifyOrganization(UUID id, OrganizationUpdate.Request request, MultipartFile file);

    OrganizationGet.Response removeOrganization(UUID id);
}