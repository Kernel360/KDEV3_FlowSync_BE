package com.checkping.dto;

import com.checkping.domain.member.Organization;
import lombok.*;

public class OrganizationGet {
    /*
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

        private String id;
        private String type;
        private String brNumber;
        private String name;
        private String brCertificateUrl;
        private String streetAddress;
        private String detailAddress;
        private String phoneNumber;
        private String status;

        public static OrganizationGet.Response toDto(Organization organization) {
            return OrganizationGet.Response.builder()
                    .id(organization.getId().toString())
                    .type(organization.getType().toString())
                    .brNumber(organization.getBrNumber())
                    .name(organization.getName())
                    .brCertificateUrl(organization.getBrCertificateUrl())
                    .streetAddress(organization.getStreetAddress())
                    .detailAddress(organization.getDetailAddress())
                    .phoneNumber(organization.getPhoneNumber())
                    .status(organization.getStatus().toString())
                    .build();
        }
    }

}