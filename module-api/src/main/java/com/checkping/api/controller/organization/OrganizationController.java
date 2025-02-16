package com.checkping.api.controller.organization;

import static com.checkping.common.enums.SuccessCode.ORGANIZATION_CHANGE_STATUS;
import static com.checkping.common.enums.SuccessCode.ORGANIZATION_REGISTER;
import static com.checkping.common.enums.SuccessCode.ORGANIZATION_UPDATE;

import com.checkping.common.dto.PageInfo;
import com.checkping.common.response.BaseResponse;
import com.checkping.dto.*;
import com.checkping.service.member.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class OrganizationController implements OrganizationApi {

    private final OrganizationService organizationService;

    @PostMapping(value = "/admins/organizations", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public BaseResponse<OrganizationCreate.Response> createOrganization(
            @Valid @RequestPart(value = "content") OrganizationCreate.Request request,
            @RequestPart(required = false, value = "file") MultipartFile file
    ) {

        OrganizationCreate.Response response = organizationService.createOrganization(request, file);

        return BaseResponse.success(response, ORGANIZATION_REGISTER.getMessage());
    }

    @GetMapping({"/admins/organizations/{organizationId}", "/organization/{organizationId}"})
    @Override
    public BaseResponse<OrganizationListGet.Response> getOrganization(@PathVariable Long organizationId) {

        OrganizationListGet.Response response = organizationService.getOrganization(organizationId);

        return BaseResponse.success(response, "업체 상세조회 성공");
    }

    @GetMapping("/admins/organizations")
    @Override
    public BaseResponse<PageInfo.Response<OrganizationListGet.Response>> getListOrganization(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword
    ) {

        PageInfo.Request request = new PageInfo.Request(currentPage, pageSize, keyword);

        PageInfo.Response<OrganizationListGet.Response> list = organizationService.getListOrganization(type, status, sortField, sortDirection, request);
        return BaseResponse.success(list, "업체 조회 성공");
    }

    @PutMapping(value = "/admins/organizations/{organizationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public BaseResponse<OrganizationUpdate.Response> modifyOrganization(
            @PathVariable Long organizationId,
            @Valid @RequestPart(value = "content") OrganizationUpdate.Request request,
            @RequestPart(required = false, value = "file") MultipartFile file) {

        OrganizationUpdate.Response response = organizationService.modifyOrganization(
                organizationId,
                request, file);

        return BaseResponse.success(response, ORGANIZATION_UPDATE.getMessage());
    }

    @PostMapping("/admins/organizations/{organizationId}/remove")
    @Override
    public BaseResponse<String> removeOrganization(
            @PathVariable Long organizationId,
            @RequestBody OrganizationDelete.Request request
    ) {

        organizationService.removeOrganization(organizationId, request);

        return BaseResponse.success(ORGANIZATION_REGISTER.getMessage(), ORGANIZATION_REGISTER.getMessage());
    }

    @PostMapping("/admins/organizations/{organizationId}/changeStatus")
    @Override
    public BaseResponse<String> changeStatusOrganization(
            @PathVariable Long organizationId,
            @RequestBody OrganizationDelete.Request request
    ) {

        organizationService.changeStatusOrganization(organizationId, request);

        return BaseResponse.success(ORGANIZATION_CHANGE_STATUS.getMessage(), ORGANIZATION_CHANGE_STATUS.getMessage());
    }

    @GetMapping({"/admins/organizations/{organizationId}/projects","/organizations/{organizationId}/projects"})
    @Override
    public BaseResponse<PageInfo.Response<ProjectListGet.Response>> getProjectsByOrganization(
            @PathVariable Long organizationId,
            @RequestParam(required = false) String managementStep,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword) {

        PageInfo.Request request = new PageInfo.Request(currentPage, pageSize, keyword);

        PageInfo.Response<ProjectListGet.Response> list = organizationService.getListProjectByOrganization(organizationId, managementStep, request);

        return BaseResponse.success(list, "업체가 속한 프로젝트 목록 조회 성공");
    }
}