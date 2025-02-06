package com.checkping.dto.project;

import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.project.ProgressStep;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressStepGet {

    @Getter
    public static class Response {
        /*
        id : 진행 단계 ID
        name : 단계명
        description : 단계 설명
        stepOrder : 순서
        status : 단계 상태
        startAt : 시작 일시
        closeAt : 마감 일시
        projectId : 프로젝트 ID
         */
        @Schema(description = "진행 단계 ID")
        private Long id;
        @Schema(description = "단계명")
        private String name;
        @Schema(description = "단계 설명")
        private String description;
        @Schema(description = "순서")
        private Integer stepOrder;
        @Schema(description = "단계 상태")
        private String status;
        @Schema(description = "시작 일시")
        private String startAt;
        @Schema(description = "마감 일시")
        private String closeAt;
        @Schema(description = "프로젝트 ID")
        private Long projectId;

        /**
         * ProgressStep Entity -> ProgressStepGet.Response Dto
         *
         * @param progressStep  ProgressStep Entity
         * @return  ProgressStepGet.Response Dto
         */
        public static Response toDto(ProgressStep progressStep) {
            Response response = new Response();
            response.id = progressStep.getId();
            response.name = progressStep.getName();
            response.description = progressStep.getDescription();
            response.stepOrder = progressStep.getStepOrder();
            response.status = progressStep.getStatus().name();
            response.startAt = DateTimeUtils.format(progressStep.getStartAt());
            response.closeAt = DateTimeUtils.format(progressStep.getCloseAt());
            response.projectId = progressStep.getProjectId();
            return response;
        }
    }
}
