package com.checkping.api.controller.organization;

import com.checkping.common.dto.PageInfo;
import com.checkping.common.response.BaseResponse;
import com.checkping.dto.OrganizationCreate;
import com.checkping.dto.OrganizationGet;
import com.checkping.dto.OrganizationUpdate;
import com.checkping.service.member.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrganizationController implements OrganizationApi {

    private final OrganizationService organizationService;

    @PostMapping(value = "/admins/organizations", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public BaseResponse<OrganizationCreate.Response> createOrganization(
            @RequestPart(value = "content") OrganizationCreate.Request request,
            @RequestPart(required = false, value = "file") MultipartFile file
    ) {

        OrganizationCreate.Response response = organizationService.createOrganization(request, file);

        return BaseResponse.success(response, "업체 생성 성공");
    }

    @GetMapping({"/admins/organizations/{organizationId}", "/organization/{organizationId}"})
    @Override
    public BaseResponse<OrganizationGet.Response> getOrganization(@PathVariable UUID organizationId) {

        OrganizationGet.Response response = organizationService.getOrganization(organizationId);

        return BaseResponse.success(response, "업체 상세조회 성공");
    }

    @GetMapping("/admins/organizations")
    @Override
    public BaseResponse<PageInfo.Response<OrganizationGet.Response>> getListOrganization(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            PageInfo.Request pageRequest
    ) {
        PageInfo.Response<OrganizationGet.Response> list = organizationService.getListOrganization(type, status, pageRequest);
        return BaseResponse.success(list, "업체 조회 성공");
    }

    @PutMapping(value = "/admins/organizations/{organizationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public BaseResponse<OrganizationUpdate.Response> modifyOrganization(
            @PathVariable UUID organizationId,
            @RequestPart(value = "content") OrganizationUpdate.Request request,
            @RequestPart(required = false, value = "file") MultipartFile file) {

        OrganizationUpdate.Response response = organizationService.modifyOrganization(
                organizationId,
                request, file);

        return BaseResponse.success(response, "업체 수정 성공");
    }

    @PatchMapping("/admins/organizations/{organizationId}/remove")
    @Override
    public BaseResponse<OrganizationGet.Response> removeOrganization(@PathVariable UUID organizationId) {

        OrganizationGet.Response response = organizationService.removeOrganization(organizationId);

        return BaseResponse.success(response, "업체 삭제 완료");
    }

}