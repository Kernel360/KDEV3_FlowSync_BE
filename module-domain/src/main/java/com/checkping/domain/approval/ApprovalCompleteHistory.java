package com.checkping.domain.approval;

import com.checkping.domain.BaseEntity;
import com.checkping.domain.member.Member;
import com.checkping.domain.project.ProgressStep;
import com.checkping.domain.project.Project;
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
    project : 프로젝트(FK : project_id)
    approval : 결재(FK : approval_id)
    progress_step : 진행 단계(FK : progress_step_id)
    actor : 작업자(FK : actor_id)
    status : 진행 상태
    reg_at : 등록 일시
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_id", nullable = false)
    private Approval approval;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progress_step_id", nullable = false)
    private ProgressStep progressStep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    private Member actor;

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

    public static ApprovalCompleteHistory generate(Project project, ProgressStep progressStep, Approval approval, Status status,
        Member actor) {
        ApprovalCompleteHistory entity = new ApprovalCompleteHistory();
        entity.project = project;
        entity.approval = approval;
        entity.progressStep = progressStep;
        entity.status = status;
        entity.actor = actor;
        return entity;
    }

    public static ApprovalCompleteHistory generate(Approval approval, Status status, Member actor) {
        return generate(approval.getProject(), approval.getProgressStep(), approval, status, actor);
    }
}
