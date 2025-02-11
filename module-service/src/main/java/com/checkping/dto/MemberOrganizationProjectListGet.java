package com.checkping.dto;

import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.member.projection.ProjectListGet;
import lombok.*;

public class MemberOrganizationProjectListGet {

    @Getter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        /*
        name : 프로젝트 이름
        developerName : 개발사
        customerName : 고객사
        managementStep : 프로젝트 관리 단계 * CONTRACT(계약), IN_PROGRESS(진행중), COMPLETED(납품완료), MAINTENANCE(하자보수), PAUSED(일시중단), DELETED(삭제)
        startAt : 프로젝트 시작 일시
        closeAt : 프로젝트 종료 일시
        updateAt : 프로젝트 수정 일시
        */
        private Long id;
        private String name;
        private String customerName;
        private String developerName;
        private String managementStep;
        private String startAt;
        private String closeAt;
        private String updateAt;

        public static Response toDto(ProjectListGet projectListGet) {
            return Response.builder()
                    .id(projectListGet.getId())
                    .name(projectListGet.getName())
                    .customerName(projectListGet.getCustomerName())
                    .developerName(projectListGet.getDeveloperName())
                    .managementStep(projectListGet.getManagementStep().toString())
                    .startAt(DateTimeUtils.format(projectListGet.getStartAt()))
                    .closeAt(DateTimeUtils.format(projectListGet.getCloseAt()))
                    .updateAt(DateTimeUtils.format(projectListGet.getCloseAt()))
                    .build();
        }


    }

}