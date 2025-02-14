package com.checkping.dto;

import com.checkping.domain.member.Organization;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class OrganizationDelete {
    /*
    reason : 삭제 사유
     */
    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @Schema(description = "삭제 사유", defaultValue = "")
        String reason ="";

    }

    /*
   id : 업체 ID
   type : 업체유형
   brNumber : 사업자등록번호
   name : 업체명
   streetAddress : 도로명주소
   detailAddress : 상세주소
   phoneNumber : 전화번호
   status : 업체 상태
   */
    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        @Schema(description = "업체 ID", example = "1")
        private String id;
        @Schema(description = "업체 유형", example = "CUSTOMER")
        private String type;
        @Schema(description = "사업자 등록번호", example = "123-45-67890")
        private String brNumber;
        @Schema(description = "업체명", example = "커널360")
        private String name;
        @Schema(description = "사업자 등록증 이미지 링크", example = "http://www.naver.com")
        private String brCertificateUrl;
        @Schema(description = "도로명 주소", example = "서울시 강남구")
        private String streetAddress;
        @Schema(description = "상세 주소", example = "역삼동")
        private String detailAddress;
        @Schema(description = "전화번호", example = "010-1234-5678")
        private String phoneNumber;
        @Schema(description = "업체 상태", example = "INACTIVE")
        private String status;
        @Schema(description = "삭제 사유")
        private String reasonForDeleteOrganization;

        public static OrganizationDelete.Response toDto(Organization organization) {
            return Response.builder()
                    .id(organization.getId().toString())
                    .type(organization.getType().toString())
                    .brNumber(organization.getBrNumber())
                    .name(organization.getName())
                    .brCertificateUrl(organization.getBrCertificateUrl())
                    .streetAddress(organization.getStreetAddress())
                    .detailAddress(organization.getDetailAddress())
                    .phoneNumber(organization.getPhoneNumber())
                    .status(organization.getStatus().toString())
                    .reasonForDeleteOrganization(organization.getReasonForDeleteOrganization())
                    .build();
        }
    }

}
