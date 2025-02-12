package com.checkping.dto;

import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.member.projection.ProjectList;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class ProjectListGet {

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
       startAt : 시작일
       deadlineAt : 예정 마감일
       closeAt : 납품 완료일
       updateAt : 수정일
        */
        @Schema(description = "프로젝트 ID", example = "1")
        private Long id;
        @Schema(description = "프로젝트 이름", example = "프로젝트1")
        private String name;
        @Schema(description = "고객사", example = "고객사1")
        private String customerName;
        @Schema(description = "개발사", example = "개발사1")
        private String developerName;
        @Schema(description = "프로젝트 관리 단계", example = "IN_PROGRESS")
        private String managementStep;
        @Schema(description = "시작일", example = "2025-01-01")
        private String startAt;
        @Schema(description = "납품 완료일", example = "2025-02-01")
        private String closeAt;
        @Schema(description = "수정일", example = "2021-02-01")
        private String updateAt;
        @Schema(description = "예정 마감일", example = "2021-01-30")
        private String deadlineAt;

        public static Response toDto(ProjectList projectList) {
            return Response.builder()
                    .id(projectList.getId())
                    .name(projectList.getName())
                    .customerName(projectList.getCustomerName())
                    .developerName(projectList.getDeveloperName())
                    .managementStep(projectList.getManagementStep().toString())
                    .startAt(DateTimeUtils.format(projectList.getStartAt()))
                    .closeAt(DateTimeUtils.format(projectList.getCloseAt()))
                    .updateAt(DateTimeUtils.format(projectList.getCloseAt()))
                    .deadlineAt(DateTimeUtils.format(projectList.getDeadlineAt()))
                    .build();
        }


    }

}