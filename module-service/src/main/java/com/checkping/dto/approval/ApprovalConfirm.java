package com.checkping.dto.approval;

import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.approval.Approval;
import com.checkping.domain.approval.Approval.ApprovalStatus;
import com.checkping.dto.member.response.MemberResponseDto;
import com.checkping.exception.approval.ApprovalStatusException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalConfirm {

    @Getter
    public static class Request {

        /*
        status : 변경할 결재 상태
         */
        @Schema(description = "변경할 결재 상태", example = "REJECTED, APPROVED")
        private String status;

        /**
         * Approval.ApprovalStatus 로 변환
         *
         * @return Approval.ApprovalStatus
         */
        public Approval.ApprovalStatus getStatus() {
            return convertStatus(this.status);
        }
    }

    @Getter
    public static class Response {

        /*
        projectId : 프로젝트 ID
        approvalId : 결재 ID
        status : 변경된 결재 상태
        category : 결재 카테고리
        approverAt : 승인 일시
        approver : 승인자
         */
        @Schema(description = "프로젝트 ID", example = "1")
        private Long projectId;
        @Schema(description = "결재 ID", example = "1")
        private Long approvalId;
        @Schema(description = "변경된 결재 상태", example = "REJECTED, APPROVED")
        private String status;
        @Schema(description = "결재 카테고리", example = "NORMAL_REQUEST, COMPLETE_REQUEST")
        private String category;
        @Schema(description = "승인 일시", example = "2021-07-01T00:00:00")
        private String approverAt;
        @Schema(description = "승인자")
        private MemberResponseDto.MeWithSignatureResponseDto approver;

        /**
         * Approval 엔티티를 Response DTO로 변환
         *
         * @param approval Approval 엔티티
         * @return Response DTO
         */
        public static Response toDto(Approval approval) {
            Response response = new Response();
            response.projectId = approval.getProject().getId();
            response.approvalId = approval.getId();
            response.status = approval.getStatus().name();
            response.category = approval.getCategory().name();

            response.approverAt = null;
            response.approver = null;

            if (!approval.isWaitStatus()) {
                response.approverAt = DateTimeUtils.format(approval.getApproverAt());
                response.approver = MemberResponseDto.MeWithSignatureResponseDto.fromEntity(approval.getApprover());
            }

            return response;
        }
    }

    /**
     * Enum : ApprovalStatus 변환 함수
     *
     * @param value ApprovalStatus 로 변환할 문자열
     * @return ApprovalStatus
     */
    public static ApprovalStatus convertStatus(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            return ApprovalStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApprovalStatusException(value);
        }
    }

}
