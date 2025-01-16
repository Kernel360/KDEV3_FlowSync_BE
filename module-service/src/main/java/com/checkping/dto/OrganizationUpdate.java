package com.checkping.dto;

import com.checkping.domain.member.Organization;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "사업자 등록번호", example = "123-45-67890")
        private String brNumber;
        @Schema(description = "사업자 등록증 이미지 링크", example = "http://www.naver.com")
        private String brCertificateUrl;
        @Schema(description = "도로명 주소", example = "서울시 강남구")
        private String streetAddress;
        @Schema(description = "상세 주소", example = "역삼동")
        private String detailAddress;
        @Schema(description = "전화번호", example = "010-1234-5678")
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