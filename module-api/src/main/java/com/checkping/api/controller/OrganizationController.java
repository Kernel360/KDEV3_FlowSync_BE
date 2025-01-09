package com.checkping.api.controller;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.OrganizationRequest;
import com.checkping.dto.OrganizationResponse;
import com.checkping.service.member.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Log4j2
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping("/admins/organizations")
    public BaseResponse<Boolean> createOrganization(@RequestBody OrganizationRequest.OrganizationCreateRequest request) {

        organizationService.createOrganization(request);

        return BaseResponse.success("업체 생성 성공");
    }

    @GetMapping({"/admins/organizations/{organization_id}", "/organization/{organization_id}"})
    public BaseResponse<OrganizationResponse.OrganizationReadResponse> getOrganization(@PathVariable UUID organization_id) {

        OrganizationResponse.OrganizationReadResponse response = organizationService.getOrganization(organization_id);

        return BaseResponse.success(response, "업체 조회 성공");
    }

    @GetMapping("/admins/organizations")
    public BaseResponse<List<OrganizationResponse.OrganizationReadResponse>> getByTypeOrAllOrganization(
            @RequestParam(required = false) String type) {

        List<OrganizationResponse.OrganizationReadResponse> list;

        if (type != null) {
            list = organizationService.getByTypeOrganizations(type);
            return BaseResponse.success(list, "타입별 업체 조회 성공");
        }
        list = organizationService.getAllOrganizations();
        return BaseResponse.success(list, "업체 전체 조회 성공");
    }

    @PutMapping("/admins/organizations/{organization_id}")
    public BaseResponse<OrganizationResponse.OrganizationUpdateResponse> modifyOrganization(
            @PathVariable UUID organization_id,
            @RequestBody OrganizationRequest.OrganizationUpdateRequest request) {

      OrganizationResponse.OrganizationUpdateResponse response = organizationService.modifyOrganization(
              organization_id,
              request);

        return BaseResponse.success(response, "업체 수정 성공");
    }

}