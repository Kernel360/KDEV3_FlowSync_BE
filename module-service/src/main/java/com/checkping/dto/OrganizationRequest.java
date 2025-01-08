package com.checkping.dto;

import com.checkping.domain.member.Organization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrganizationRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrganizationSignUpRequest {

        /*
          type : 업체유형
          brNumber : 사업자등록번호
          name : 업체명
          brCertificateUrl : 사업자등록증 이미지 링크
          streetAddress : 도로명주소
          detailAddress : 상세주소
          phoneNumber : 전화번호
         */

        private String type;
        private String brNumber;
        private String name;
        private String brCertificateUrl;
        private String streetAddress;
        private String detailAddress;
        private String phoneNumber;

        public static Organization toEntity(OrganizationSignUpRequest request) {
            return Organization.builder()
                    .type(Organization.Type.valueOf(request.type))
                    .brNumber(request.brNumber)
                    .name(request.name)
                    .brCertificateUrl(request.brCertificateUrl)
                    .streetAddress(request.streetAddress)
                    .detailAddress(request.detailAddress)
                    .phoneNumber(request.phoneNumber)
                    .status(Organization.Status.ACTIVE)
                    .build();
        }

    }

}