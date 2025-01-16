package com.checkping.api.controller.organization;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.*;
import com.checkping.service.member.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrganizationController implements OrganizationApi {

    private final OrganizationService organizationService;

    @PostMapping("/admins/organizations")
    @Override
    public BaseResponse<OrganizationCreate.Response> createOrganization(@RequestBody OrganizationCreate.Request request) {

        OrganizationCreate.Response response = organizationService.createOrganization(request);

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
    public BaseResponse<List<OrganizationGet.Response>> getByTypeOrganization(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status
    ) {
        List<OrganizationGet.Response> list = organizationService.getAllByTypeAndStatusOrganizations(type, status);
        return BaseResponse.success(list, "업체 조회 성공");
    }

    @PutMapping("/admins/organizations/{organizationId}")
    @Override
    public BaseResponse<OrganizationUpdate.Response> modifyOrganization(
            @PathVariable UUID organizationId,
            @RequestBody OrganizationUpdate.Request request) {

        OrganizationUpdate.Response response = organizationService.modifyOrganization(
                organizationId,
                request);

        return BaseResponse.success(response, "업체 수정 성공");
    }

    @PutMapping("/admins/organizations/{organizationId}/remove")
    @Override
    public BaseResponse<OrganizationGet.Response> removeOrganization(@PathVariable UUID organizationId) {

        OrganizationGet.Response response = organizationService.removeOrganization(organizationId);

        return BaseResponse.success(response, "업체 삭제 완료");
    }

}