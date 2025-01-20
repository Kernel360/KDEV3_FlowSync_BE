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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Table(name = "approval")
@Entity
public class Approval extends BaseEntity {
    /*
    id : id
    project : 프로젝트(FK : project_id)
    progress_step_id : 프로젝트 진행 단계 id
    title : 제목
    content : 내용
    status : 결재 상태
    register : 작성자 (FK : register_id)
    register_name : 작성자 이름
    cancle_at : 취소 일자
    approver_at : 승인 일시
    approver_id : 승인자 id
    approver_name : 승인자 이름
    updated_at : 수정 일시
    reg_at : 작성 일시
    deleted_yn : 삭제 여부
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progress_step_id")
    private ProgressStep progressStep;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApprovalStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "register_id", nullable = false)
    private Member register;

    @Column(name = "register_name")
    private String registerName;

    @Column(name = "cancel_at")
    private LocalDateTime cancelAt;

    @Column(name = "approver_at")
    private LocalDateTime approverAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private Member approver;

    @Column(name = "approver_name")
    private String approverName;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "approval", fetch = FetchType.LAZY)
    private List<ApprovalComment> commentList;

    @OneToMany(mappedBy = "approval", fetch = FetchType.LAZY)
    private List<ApprovalLink> linkList;

    @OneToMany(mappedBy = "approval", fetch = FetchType.LAZY)
    private List<ApprovalFile>  fileList;

    /*
    ENUM
     */

    @Enumerated(EnumType.STRING)
    @Column(name = "deleted_yn", nullable = false)
    private DeleteStatus deleteYn;

    @Getter
    @RequiredArgsConstructor
    public enum ApprovalStatus {
        WAIT("대기"), REJECTED("반려"), APPROVED("승인");
        private final String description;
    }

    @Getter
    @RequiredArgsConstructor
    public enum DeleteStatus {
        Y("비활성화"), N("활성화");
        private final String description;
    }
}
