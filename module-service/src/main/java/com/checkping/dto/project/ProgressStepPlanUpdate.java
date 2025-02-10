package com.checkping.dto.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressStepPlanUpdate {

    @Getter
    public static class Request {
        /*
        startAt : 시작 일시
        deadlineAt : 예상 마감 일시
         */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startAt;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime deadlineAt;
    }

    @Getter
    public static class Response {
        /*
        id : id
        name : 단계명
        description : 단계 설명
        stepOrder : 순서
        status : 진행 단계 상태
        startAt : 시작 일시
        closeAt : 마감 일시
        deadlineAt : 예상 마감 일시
        projectId : 프로젝트 (FK : project_id)
        relatedApprovalId : 관련 결재 (FK : approval_id)
         */
        private Long id;
        private String name;
        private String description;
        private Integer stepOrder;
        private String status;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startAt;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime closeAt;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime deadlineAt;
        private Long projectId;
        private Long relatedApprovalId;
    }
}
