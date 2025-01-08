package com.checkping.api.controller;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.OrganizationRequest;
import com.checkping.service.member.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrganizationContoller {

    private final OrganizationService organizationService;

    @PostMapping("/admins/organizations")
    public BaseResponse<Boolean> getMember(@RequestBody OrganizationRequest.OrganizationCreateRequest request) {

        organizationService.createOrganization(request);

        return BaseResponse.success("업체 생성 성공");
    }


}
