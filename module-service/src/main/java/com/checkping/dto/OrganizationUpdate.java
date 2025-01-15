package com.checkping.dto;

import com.checkping.domain.member.Organization;
import lombok.*;

public class OrganizationUpdate {

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        /*
        brNumber : 사업자등록번호
        brCertificateUrl : 사업자등록증 이미지 링크
        streetAddress : 도로명주소
        detailAddress : 상세주소
        phoneNumber : 전화번호
         */
        private String brNumber;
        private String brCertificateUrl;
        private String streetAddress;
        private String detailAddress;
        private String phoneNumber;

        public static Organization toEntity(OrganizationUpdate.Request request) {
            return Organization.builder()
                    .brNumber(request.brNumber)
                    .brCertificateUrl(request.brCertificateUrl)
                    .streetAddress(request.streetAddress)
                    .detailAddress(request.detailAddress)
                    .phoneNumber(request.phoneNumber)
                    .build();
        }
    }

    /*
    type : 업체유형
    brNumber : 사업자등록번호
    name : 업체명
    brCertificateUrl : 사업자등록증 이미지 링크
    streetAddress : 도로명주소
    detailAddress : 상세주소
    phoneNumber : 전화번호
    */
    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private String type;
        private String brNumber;
        private String name;
        private String brCertificateUrl;
        private String streetAddress;
        private String detailAddress;
        private String phoneNumber;

        public static OrganizationUpdate.Response toDto(Organization organization) {
            return OrganizationUpdate.Response.builder()
                    .type(organization.getType().toString())
                    .brNumber(organization.getBrNumber())
                    .name(organization.getName())
                    .brCertificateUrl(organization.getBrCertificateUrl())
                    .streetAddress(organization.getStreetAddress())
                    .detailAddress(organization.getDetailAddress())
                    .phoneNumber(organization.getPhoneNumber())
                    .build();
        }
    }

}