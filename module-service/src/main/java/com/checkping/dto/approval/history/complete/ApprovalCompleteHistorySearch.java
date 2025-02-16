package com.checkping.dto.approval.history.complete;

import com.checkping.common.response.PaginationProps;
import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.approval.ApprovalCompleteHistory;
import com.checkping.dto.member.response.MemberResponseDto;
import com.checkping.dto.project.ProgressStepGet;
import com.checkping.info.approval.ApprovalCompleteHistorySearchInfo;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalCompleteHistorySearch {

    @Getter
    @Setter
    public static class Condition {
        /*
        progressStepId : 진행 단계 ID
        currentPage : 현재 페이지
        pageSize : 페이지 사이즈
         */
        private Long progressStepId;
        @Min(1)
        private Integer currentPage = 1;
        @Min(5)
        private Integer pageSize = 10;

        /**
         * ApprovalCompleteHistorySearchCondition Dto -> ApprovalCompleteHistorySearchInfo
         * Dto 변환
         *
         * @return  ApprovalCompleteHistorySearchInfo
         */
        public ApprovalCompleteHistorySearchInfo toInfo() {
            return new ApprovalCompleteHistorySearchInfo(progressStepId, currentPage - 1, pageSize);
        }
    }

    @Getter
    public static class Item {
        /*
        id : id
        projectId : 프로젝트 ID
        approvalId : 결재 ID
        approvalName : 결재 이름
        actor : 행위자
        progress_step : 진행 단계(FK : progress_step_id)
        regAt : 로그 등록 일자
        status : 진행 상태
         */
        private Long id;
        private Long projectId;
        private Long approvalId;
        private String approvalName;
        private MemberResponseDto.MeResponseDto actor;
        private ProgressStepGet.Response progressStep;
        private String regAt;
        private String status;

        /**
         * ApprovalCompleteHistory Entity -> ApprovalCompleteHistoryItem Dto
         *
         * @param approvalCompleteHistory ApprovalCompleteHistory Entity
         * @return ApprovalCompleteHistoryItem Dto
         */
        public static Item toDto(ApprovalCompleteHistory approvalCompleteHistory) {
            Item dto = new Item();
            dto.id = approvalCompleteHistory.getId();
            dto.projectId = approvalCompleteHistory.getApproval().getProject().getId();
            dto.approvalId = approvalCompleteHistory.getApproval().getId();
            dto.approvalName = approvalCompleteHistory.getApproval().getTitle();
            dto.actor = MemberResponseDto.MeResponseDto.fromEntity(
                approvalCompleteHistory.getActor());
            dto.progressStep = ProgressStepGet.Response.toDto(
                approvalCompleteHistory.getProgressStep());
            dto.regAt = DateTimeUtils.format(approvalCompleteHistory.getRegAt());
            dto.status = approvalCompleteHistory.getStatus().name();
            return dto;
        }

        /**
         * ApprovalCompleteHistory Entity List -> ApprovalCompleteHistoryItem Dto List
         *
         * @param approvalCompleteHistories ApprovalCompleteHistory Entity List
         * @return ApprovalCompleteHistoryItem Dto List
         */
        public static List<Item> toDto(List<ApprovalCompleteHistory> approvalCompleteHistories) {
            if (CollectionUtils.isEmpty(approvalCompleteHistories)) {
                return List.of();
            }

            return approvalCompleteHistories.stream().map(Item::toDto).toList();
        }
    }

    @Getter
    public static class Response {
        /*
        completionHistories : 결재 완료 이력 목록
        meta : 페이징 정보
         */
        private List<Item> completionHistories;
        private PaginationProps meta;

        public static Response toDto(Page<ApprovalCompleteHistory> page) {
            Response response = new Response();
            response.completionHistories = page.map(Item::toDto).getContent();
            response.meta = PaginationProps.toDto(page);
            return response;
        }

    }
}
