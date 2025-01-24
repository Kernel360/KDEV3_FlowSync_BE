package com.checkping.domain.project;


import com.checkping.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Table(name = "progress_step")
@Entity
@AllArgsConstructor
@NoArgsConstructor
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
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "step_order")
    private Integer stepOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Project.Status status;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "close_at")
    private LocalDateTime closeAt;

    @Column(name = "project_id")
    private Long projectId;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;*/

    @Getter
    @RequiredArgsConstructor
    public enum ProgressStatus {
        NOT_STARTED, IN_PROGRESS, COMPLETED, CANCELED
    }

    @Getter
    @RequiredArgsConstructor
    public enum CurrentStep {
        REQUIREMENTS("요구사항 정의"),
        SCREEN_DESIGN("화면설계"),
        DESIGN( "디자인" ),
        PUBLISHING("퍼블리싱"),
        DEVELOPMENT("개발"),
        REVIEW("검수");

        private final String description;
    }
}
