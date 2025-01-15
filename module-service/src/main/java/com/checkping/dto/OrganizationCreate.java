package com.checkping.dto;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.CustomException;
import com.checkping.domain.member.Organization;
import lombok.*;

public class OrganizationCreate {

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        /*
        type : 업체유형
        status : 업체상태
        brNumber : 사업자등록번호
        name : 업체명
        brCertificateUrl : 사업자등록증 이미지 링크
        streetAddress : 도로명주소
        detailAddress : 상세주소
        phoneNumber : 전화번호
         */
        private String type;
        private String status;
        private String brNumber;
        private String name;
        private String brCertificateUrl;
        private String streetAddress;
        private String detailAddress;
        private String phoneNumber;

        public Organization.Type getTypeEnum() {
            try {
                return Organization.Type.valueOf(this.type.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new CustomException(ErrorCode.BAD_REQUEST);
            }
        }

        public static Organization toEntity(OrganizationCreate.Request request) {
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

        public static OrganizationCreate.Response toDto(Organization organization) {
            return OrganizationCreate.Response.builder()
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