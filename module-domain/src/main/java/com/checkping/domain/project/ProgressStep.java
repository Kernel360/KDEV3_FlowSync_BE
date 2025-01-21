package com.checkping.domain.project;


import com.checkping.domain.BaseEntity;
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

@Getter
@Table(name = "progress_step")
@Entity
public class ProgressStep extends BaseEntity {
    /*
    id : id
    name : 단계명
    description : 단계 설명
    order : 순서
    status : 단계 상태
    start_at : 시작 일시
    close_at : 마감 일시
    project : 프로젝트 (FK : project_id)
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Project.Status status;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "close_at", nullable = false)
    private LocalDateTime closeAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Getter
    @RequiredArgsConstructor
    public enum ProgressStatus {
        NOT_STARTED, IN_PROGRESS, COMPLETED, CANCELED
    }
}
