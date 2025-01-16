package com.checkping.api.controller.organization;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.OrganizationCreate;
import com.checkping.dto.OrganizationGet;
import com.checkping.dto.OrganizationUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Tag(name = "업체 API(OrganizationController)", description = "업체 API 입니다.")
public interface OrganizationApi {

    @Operation(summary = "업체 생성", description = "업체 생성하는 기능입니다. 파일 첨부 미구현")
    BaseResponse<OrganizationCreate.Response> createOrganization(
            @Parameter(description = "업체 생성 Request") OrganizationCreate.Request request);

    @Operation(summary = "업체 상세 조회", description = "업체 상세 조회 기능입니다.")
    BaseResponse<OrganizationGet.Response> getOrganization(@Parameter(description = "업체 ID")UUID organizationId);

    @Operation(summary = "업체 전체 조회", description = "업체 조회 기능입니다.")
    BaseResponse<List<OrganizationGet.Response>> getByTypeOrganization(
            @Parameter(description = "업체 타입") @RequestParam(required = false) String type,
            @Parameter(description = "업체 상태") @RequestParam(required = false) String status
    );

    @Operation(summary = "업체 수정", description = "업체 정보 수정 기능입니다.")
    BaseResponse<OrganizationUpdate.Response> modifyOrganization(
            @Parameter(description = "업체 ID") @PathVariable UUID organizationId,
            @Parameter(description = "업체 수정 Request") OrganizationUpdate.Request request);

    @Operation(summary = "업체 삭제", description = "업체 정보 삭제 기능입니다.")
    BaseResponse<OrganizationGet.Response> removeOrganization(
            @Parameter(description = "업체 ID") @PathVariable UUID organizationId
    );

}