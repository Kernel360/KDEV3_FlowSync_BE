package com.checkping.api.controller.organization;

import com.checkping.common.dto.PageInfo;
import com.checkping.common.response.BaseResponse;
import com.checkping.dto.OrganizationCreate;
import com.checkping.dto.OrganizationDelete;
import com.checkping.dto.OrganizationListGet;
import com.checkping.dto.OrganizationUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "업체 API(OrganizationController)", description = "업체 API 입니다.")
public interface OrganizationApi {

    @Operation(summary = "업체 생성", description = "업체 생성하는 기능입니다. 파일 첨부 미구현")
    BaseResponse<OrganizationCreate.Response> createOrganization(
            @Parameter(description = "업체 생성 Request", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) @RequestPart OrganizationCreate.Request request,
            @Parameter(description = "첨부 파일", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart MultipartFile file
    );

    @Operation(summary = "업체 상세 조회", description = "업체 상세 조회 기능입니다.")
    BaseResponse<OrganizationListGet.Response> getOrganization(@Parameter(description = "업체 ID") Long organizationId);

    @Operation(summary = "업체 전체 조회", description = "업체 조회 기능입니다.")
    BaseResponse<PageInfo.Response<OrganizationListGet.Response>> getListOrganization(
            @Parameter(description = "업체 타입(CUSTOMER / DEVELOPER)") @RequestParam(required = false) String type,
            @Parameter(description = "업체 상태(ACTIVE / INACTIVE") @RequestParam(required = false) String status,
            @Parameter(description = "페이지 번호") @RequestParam int page,
            @Parameter(description = "게시글 수") @RequestParam int size,
            @Parameter(description = "검색어") @RequestParam String keyword

    );

    @Operation(summary = "업체 수정", description = "업체 정보 수정 기능입니다.")
    BaseResponse<OrganizationUpdate.Response> modifyOrganization(
            @Parameter(description = "업체 ID") @PathVariable Long organizationId,
            @Parameter(description = "업체 수정 Request", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)) @RequestPart OrganizationUpdate.Request request,
            @Parameter(description = "첨부 파일", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart MultipartFile file);

    @Operation(summary = "업체 삭제", description = "업체 정보 삭제 기능입니다.")
    BaseResponse<String> removeOrganization(
            @Parameter(description = "업체 ID") @PathVariable Long organizationId,
            @Parameter(description = "삭제 사유") @RequestBody OrganizationDelete.Request request
            );

    @Operation(summary = "업체 상태 변경", description = "업체 활성화 상태 전환 기능입니다.")
    BaseResponse<String> changeStatusOrganization(
            @Parameter(description = "업체 ID") @PathVariable Long organizationId,
            @Parameter(description = "전환 사유") @RequestBody OrganizationDelete.Request request
    );

}