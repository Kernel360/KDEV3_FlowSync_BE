package com.checkping.dto.approval;

import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.approval.Approval;
import com.checkping.dto.approval.ApprovalConfirm.Response;
import com.checkping.dto.member.response.MemberResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalReject {

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
        public static ApprovalReject.Response toDto(Approval approval) {
            ApprovalReject.Response response = new ApprovalReject.Response();
            response.projectId = approval.getProject().getId();
            response.approvalId = approval.getId();
            response.status = approval.getStatus().name();
            response.category = approval.getCategory().name();

            response.approverAt = null;
            response.approver = null;

            if (!approval.isWaitStatus()) {
                response.approverAt = DateTimeUtils.format(approval.getApproverAt());
                response.approver = MemberResponseDto.MeWithSignatureResponseDto.fromEntity(
                    approval.getApprover());
            }

            return response;
        }

    }

}
