package com.checkping.dto.approval;

import com.checkping.domain.project.ProgressStep;
import com.checkping.info.approval.ApprovalCountProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalCount {

    @Getter
    public static class Response implements Comparable<ApprovalCount.Response> {

        /*
        id : progressStep ID
        title : progressStep 제목
        value : progressStep 값
        count : progressStep 카운트
        status : progressStep 상태
        stepOrder : progressStep 순서
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
        @Schema(description = "progressStep 순서")
        private Integer stepOrder;

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
            dto.stepOrder = -1;
            return dto;
        }

        /**
         * 도메인 모듈 - ApprovalCountProjection -> ApprovalCount.Response Dto
         *
         * @param approvalCountProjection ApprovalCountProjection
         * @return ApprovalCount.Response Dto
         */
        public static Response toDto(ApprovalCountProjection approvalCountProjection) {
            Response dto = new Response();
            dto.id = approvalCountProjection.getId();
            dto.title = approvalCountProjection.getTitle();
            dto.value = approvalCountProjection.getValue();
            dto.count = approvalCountProjection.getCount();
            dto.status = approvalCountProjection.getStatus();
            dto.stepOrder = approvalCountProjection.getStepOrder();
            return dto;
        }

        /**
         * 도메인 모듈 - ApprovalCountProjection List -> ApprovalCount.Response List Dto 전체 카운트를 추가하여 반환
         *
         * @param approvalCountProjections ApprovalCountProjection List
         * @return ApprovalCount.Response List Dto
         */
        public static List<Response> toDto(List<ApprovalCountProjection> approvalCountProjections) {

            // ApprovalCountProjection -> ApprovalCount.Response Dto
            List<Response> responseList = approvalCountProjections.stream()
                .map(ApprovalCount.Response::toDto)
                .collect(Collectors.toList());

            // 전체 카운트 추가
            Long totalCount = responseList.stream().mapToLong(Response::getCount).sum();
            responseList.add(makeEntireCount(totalCount));

            // 정렬 (오름차순)
            responseList.sort(Comparator.naturalOrder());

            return responseList;
        }

        @Override
        public int compareTo(Response o) {
            return this.id.compareTo(o.id);
        }
    }

}
