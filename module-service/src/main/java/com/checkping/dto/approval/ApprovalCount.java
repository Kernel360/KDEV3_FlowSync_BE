package com.checkping.dto.approval;

import com.checkping.domain.project.ProgressStep;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalCount {

    @Getter
    public static class Response {

        /*
        id : progressStep ID
        title : progressStep 제목
        value : progressStep 값
        count : progressStep 카운트
        status : progressStep 상태
         */
        @Schema(description = "progressStep ID")
        private Long id;
        @Schema(description = "progressStep 제목")
        private String title;
        @Schema(description = "progressStep 값")
        private String value;
        @Schema(description = "progressStep 에 해당하는 결재 글의 개수")
        private Long count;
        @Schema(description = "progressStep 상태")
        private String status;

        /**
         * ProgressStep Entity -> ApprovalCount.Response Dto
         *
         * @param progressStep ProgressStep Entity
         * @param count        progressStep 에 해당하는 결재 글의 개수
         * @return ApprovalCount.Response Dto
         */
        public static Response toDto(ProgressStep progressStep, Long count) {
            Response dto = new Response();
            dto.id = progressStep.getId();
            dto.title = progressStep.getName();
            dto.value = progressStep.getDescription();
            dto.count = count;
            dto.status = progressStep.getStatus() != null ? progressStep.getStatus().name() : null;
            return dto;
        }

        /**
         * 전체 카운트 생성
         *
         * @param count 전체 카운트
         * @return 전체 카운트 Response
         */
        public static Response makeEntireCount(Long count) {
            Response dto = new Response();
            dto.id = 0L;
            dto.title = "전체";
            dto.value = "ALL";
            dto.count = count;
            dto.status = "ALL";
            return dto;
        }
    }

}
