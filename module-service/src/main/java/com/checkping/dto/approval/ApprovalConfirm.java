package com.checkping.dto.approval;

import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.approval.Approval;
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
    }

    @Getter
    public static class Response {

        /*
        projectId : 프로젝트 ID
        approvalId : 결재 ID
        status : 변경된 결재 상태
        cancelAt : 취소 일시
        approverAt : 승인 일시
        approverId : 승인자 ID
        approverName : 승인자 이름
         */
        @Schema(description = "프로젝트 ID", example = "1")
        private Long projectId;
        @Schema(description = "결재 ID", example = "1")
        private Long approvalId;
        @Schema(description = "변경된 결재 상태", example = "REJECTED, APPROVED")
        private String status;
        @Schema(description = "취소 일시", example = "2021-07-01T00:00:00")
        private String cancelAt;
        @Schema(description = "승인 일시", example = "2021-07-01T00:00:00")
        private String approverAt;
        @Schema(description = "승인자 ID", example = "1")
        private Long approverId;
        @Schema(description = "승인자 이름", example = "홍길동")
        private String approverName;

        /**
         * Approval 엔티티를 Response DTO로 변환
         *
         * @param approval Approval 엔티티
         * @return Response DTO
         */
        public static Response toDto(Approval approval) {
            Response response = new Response();
            response.projectId = approval.getProjectId();
            response.approvalId = approval.getId();
            response.status = approval.getStatus().name();
            response.cancelAt = approval.getCancelAt() == null ? null
                : DateTimeUtils.format(approval.getCancelAt());
            response.approverAt = approval.getApproverAt() == null ? null
                : DateTimeUtils.format(approval.getApproverAt());
            response.approverId = approval.getApproverId();
            response.approverName = approval.getApproverName();
            return response;
        }
    }

}
