package com.checkping.domain.approval;

import com.checkping.domain.BaseEntity;
import com.checkping.domain.project.ProgressStep;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Table(name = "approval_complete_history")
@Entity
public class ApprovalCompleteHistory extends BaseEntity {
    /*
    id : id
    approval : 결재(FK : approval_id)
    progress_step : 진행 단계(FK : progress_step_id)
    previous_status : 이전 결재 상태
    current_status : 현재 결재 상태
    reg_at : 등록 일시
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_id", nullable = false)
    private Approval approval;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progress_step_id", nullable = false)
    private ProgressStep progressStep;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @CreatedDate
    @Column(name = "reg_at", nullable = false)
    private LocalDateTime regAt;

    @Getter
    @RequiredArgsConstructor
    public enum Status {
        CREATE("등록"),
        MODIFY("수정"),
        DELETE("삭제"),
        CONFIRM("승인"),
        REJECT("반려");

        private final String description;
    }

    public static ApprovalCompleteHistory generate(ProgressStep progressStep, Approval approval, Status status) {
        ApprovalCompleteHistory entity = new ApprovalCompleteHistory();
        entity.approval = approval;
        entity.progressStep = progressStep;
        entity.status = status;
        return entity;
    }

    public static ApprovalCompleteHistory generate(Approval approval, Status status) {
        return generate(approval.getProgressStep(), approval, status);
    }
}
