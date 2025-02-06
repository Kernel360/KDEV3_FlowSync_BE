package com.checkping.dto.approval;

import com.checkping.common.response.PaginationProps;
import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.approval.Approval;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalSearch {

    @Getter
    public static class ApprovalItem {

        /*
        id : 결재 ID
        projectId : 프로젝트 ID
        progressStepId : 프로젝트 진행 단계 ID
        title : 결재 제목
        status : 결재 상태
        registerId : 작성자 ID
        registerName : 작성자 이름
        regAt : 작성일
        updatedAt : 수정일
        approvalAt : 결재일
        approvalId : 결재자 ID
        approvalName : 결재자 이름
        cancelAt : 취소일
         */
        private Long id;
        private Long projectId;
        //TODO : 엔티티를 참조하도록 변경 필요
        private Long progressStepId;
        private String title;
        private String status;
        private Long registerId;
        private String registerName;
        private String regAt;
        private String updatedAt;
        private String approverAt;
        private Long approvalId;
        private String approvalName;
        private String cancelAt;

        /**
         * Approval Entity -> ApprovalItem Dto
         *
         * @param approval  Approval Entity
         * @return  ApprovalItem Dto
         */
        public static ApprovalItem toDto(Approval approval) {
            ApprovalItem dto = new ApprovalItem();
            dto.id = approval.getId();
            dto.projectId = approval.getProject().getId();
            dto.progressStepId = approval.getProgressStepId();
            dto.title = approval.getTitle();
            dto.status = approval.getStatus().name();
            dto.registerId = approval.getRegisterId();
            dto.registerName = approval.getRegisterName();
            dto.regAt = DateTimeUtils.format(approval.getRegAt());
            dto.updatedAt = DateTimeUtils.format(approval.getUpdatedAt());
            dto.approverAt = DateTimeUtils.format(approval.getApproverAt());
            dto.approvalId = approval.getApproverId();
            dto.approvalName = approval.getApproverName();
            dto.cancelAt = DateTimeUtils.format(approval.getCancelAt());
            return dto;
        }

        /**
         * Approval Entity List -> ApprovalItem Dto List
         *
         * @param approvals Approval Entity List
         * @return  ApprovalItem Dto List
         */
        public static List<ApprovalItem> toDto(List<Approval> approvals) {
            // null or empty check
            if (approvals == null || approvals.isEmpty()) {
                return List.of();
            }

            return approvals.stream().map(ApprovalItem::toDto).toList();
        }

    }


    @Getter
    public static class Response {
        /*
        projectApprovals : 결재 목록
        meta : 페이징 정보
         */

        private List<ApprovalItem> projectApprovals;
        private PaginationProps meta;

        public static Response toDto(Page<Approval> page) {
            Response response = new Response();
            response.projectApprovals = ApprovalSearch.ApprovalItem.toDto(page.getContent());
            response.meta = PaginationProps.toDto(page);
            return response;
        }
    }

}
