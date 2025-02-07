package com.checkping.dto;

import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.member.Organization;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class OrganizationGet {
    /*
   id : 업체 ID
   type : 업체유형
   brNumber : 사업자등록번호
   name : 업체명
   streetAddress : 도로명주소
   detailAddress : 상세주소
   phoneNumber : 전화번호
   status : 업체 상태
   regAt : 등록일시
   memberList : 회원 목록
   projectList : 프로젝트 목록
   */
    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response<T, U> {

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
        @Schema(description = "업체 상태", example = "ACTIVE")
        private String status;
        @Schema(description = "삭제 사유")
        private String reasonForDeleteOrganization;
        @Schema(description = "등록일시")
        private String regAt;
        @Schema(description = "회원 목록")
        private List<T> memberList;
        @Schema(description = "프로젝트 목록")
        private List<U> projectList;

        public static <T, U> OrganizationGet.Response<T, U> toDto(Organization organization, List<T> memberList, List<U> projectList) {
            return Response.<T, U>builder()
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
                    .regAt(DateTimeUtils.format(organization.getRegAt()))
                    .memberList(memberList)
                    .projectList(projectList)
                    .build();
        }
    }

}