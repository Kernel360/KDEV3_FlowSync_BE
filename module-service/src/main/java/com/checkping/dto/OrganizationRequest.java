package com.checkping.dto;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.CustomException;
import com.checkping.domain.member.Organization;
import lombok.*;

public class OrganizationRequest {

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
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

        public static Organization toEntity(CreateRequest request) {
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