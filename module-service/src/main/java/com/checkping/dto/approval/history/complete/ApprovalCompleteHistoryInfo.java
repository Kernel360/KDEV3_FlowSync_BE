package com.checkping.dto.approval.history.complete;

import com.checkping.domain.approval.Approval;
import com.checkping.domain.approval.ApprovalCompleteHistory;
import com.checkping.domain.project.ProgressStep;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApprovalCompleteHistoryInfo {

    /**
     * 진행 단계, 결재 정보를 이용하여 결재 완료 이력 엔티티를 생성합니다.
     *
     * @param progressStep 진행 단계
     * @param approval     결재 정보
     * @return 결재 완료 이력 엔티티
     */
    public static ApprovalCompleteHistory toEntity(ProgressStep progressStep, Approval approval, ApprovalCompleteHistory.Status status) {
        return ApprovalCompleteHistory.generate(progressStep, approval, status);
    }

    public static ApprovalCompleteHistory toEntity(Approval approval, ApprovalCompleteHistory.Status status) {
        return ApprovalCompleteHistory.generate(approval, status);
    }
}
