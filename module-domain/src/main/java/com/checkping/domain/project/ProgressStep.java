package com.checkping.domain.project;


import com.checkping.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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
    stepOrder : 순서
    status : 진행 단계 상태
    start_at : 시작 일시
    close_at : 마감 일시
    deadline_at : 예상 마감 일시
    project : 프로젝트 (FK : project_id)
    related_approval : 관련 결재 (FK : approval_id)
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
    private Status status;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "close_at")
    private LocalDateTime closeAt;

    @Column(name = "deadline_at")
    private LocalDateTime deadlineAt;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "related_approval_id")
    private Long relatedApprovalId;

    @Getter
    @RequiredArgsConstructor
    public enum Status {
        WAIT("대기"), IN_PROGRESS("진행중"), PAUSED("일시 중단"), COMPLETED("완료");

        private final String description;
    }

    @Getter
    @RequiredArgsConstructor
    public enum CurrentStep {
        REQUIREMENTS(1, "요구사항 정의", "요구사항을 수집하고 문서화하는 단계"),
        SCREEN_DESIGN(2, "화면설계", "화면의 구조와 흐름을 정의하는 단계"),
        DESIGN(3, "디자인", "UI/UX 디자인을 수행하는 단계"),
        PUBLISHING(4, "퍼블리싱", "디자인을 웹 표준에 맞춰 적용하는 단계"),
        DEVELOPMENT(5, "개발", "기능을 구현하고 시스템을 개발하는 단계"),
        REVIEW(6, "검수", "완성된 결과물을 테스트하고 검수하는 단계");

        private final Integer order;
        private final String name;
        private final String description;
    }

    /**
     * 생성 팩토리 메서드
     *
     * @param projectId   프로젝트 아이디
     * @param name        단계명
     * @param description 단계 설명
     * @param stepOrder   순서
     * @return ProgressStep Entity
     */
    public static ProgressStep generate(Long projectId, String name, String description,
        Integer stepOrder) {
        return ProgressStep.builder().projectId(projectId).name(name).description(description)
            .stepOrder(stepOrder).status(Status.WAIT).build();
    }
}
