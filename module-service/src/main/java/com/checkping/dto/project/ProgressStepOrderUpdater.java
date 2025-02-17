package com.checkping.dto.project;

import com.checkping.domain.project.ProgressStep;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressStepOrderUpdater {

    @Getter
    public static class Item {

        /*
        id : id
        order : 순서
         */
        private Long id;
        private Integer order;

        public Item(Long id, Integer order) {
            this.id = id;
            this.order = order;
        }
    }

    @Getter
    public static class Request {

        /*
        steps : 단계 목록
         */
        @Size(min = 1, message = "단계는 최소 1개 이상이어야 합니다.")
        private List<ProgressStepOrderUpdater.Item> steps;
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
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime startAt;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime closeAt;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime deadlineAt;
        private Long projectId;
        private Long relatedApprovalId = null;

        /**
         * ProgressStep Entity -> Response Dto
         *
         * @param progressStep 진행 단계 Entity
         * @return 진행 단계 Response Dto
         */
        public static Response toDto(ProgressStep progressStep) {
            Response dto = new Response();
            dto.id = progressStep.getId();
            dto.name = progressStep.getName();
            dto.description = progressStep.getDescription();
            dto.stepOrder = progressStep.getStepOrder();
            dto.status = progressStep.getStatus().name();
            dto.startAt = progressStep.getStartAt();
            dto.closeAt = progressStep.getCloseAt();
            dto.deadlineAt = progressStep.getDeadlineAt();
            dto.projectId = progressStep.getProjectId();

            if (progressStep.getRelatedApproval() != null) {
                dto.relatedApprovalId = progressStep.getRelatedApproval().getId();
            }
            return dto;
        }

        /**
         * ProgressStep List -> Response List
         *
         * @param progressSteps 진행 단계 List
         * @return  진행 단계 Response List
         */
        public static List<Response> toDto(List<ProgressStep> progressSteps) {
            return progressSteps.stream()
                .map(ProgressStepOrderUpdater.Response::toDto)
                .toList();
        }
    }
}
