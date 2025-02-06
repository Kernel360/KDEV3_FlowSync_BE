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
import jakarta.persistence.Lob;
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

    @Lob
    @Column(name = "content", columnDefinition = "TEXT", length = 65536)
    private String content;

    // TODO : 연관 관계 맵핑 필요
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "register_id")
    private Member register;

    @Column(name = "cancel_at")
    private LocalDateTime cancelAt;

    @Column(name = "approver_at")
    private LocalDateTime approverAt;

    // TODO : 연관 관계 맵핑 필요
    private Long approverId;

    @Column(name = "approver_name")
    private String approverName;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedDate
    @Column(name = "reg_at")
    private LocalDateTime regAt;

    @OneToMany(mappedBy = "approval", fetch = FetchType.LAZY)
    private List<ApprovalComment> commentList;

    @OneToMany(mappedBy = "approval", fetch = FetchType.LAZY)
    private List<ApprovalLink> linkList;

    @OneToMany(mappedBy = "approval", fetch = FetchType.LAZY)
    private List<ApprovalFile> fileList;

    /*
    ENUM
     */

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApprovalStatus status;

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

    /*
    Generate
     */

    public static Approval generate(Project project, ProgressStep progressStep, Member register,
        String title, String content) {

        Approval approval = new Approval();
        approval.title = title;
        approval.content = content;

        approval.project = project;
        approval.progressStep = progressStep;
        approval.register = register;

        // 생성 시 기본 값
        approval.status = ApprovalStatus.WAIT;
        approval.deleteYn = DeleteStatus.N;

        return approval;
    }

    /**
     * 파일 리스트 추가
     *
     * @param approvalFiles 파일 리스트
     */
    public void addFiles(List<ApprovalFile> approvalFiles) {
        this.fileList = approvalFiles;
    }

    /**
     * 링크 리스트 추가
     *
     * @param links 링크 리스트
     */
    public void addLinks(List<ApprovalLink> links) {
        this.linkList = links;
    }

    /**
     * 링크 리스트 추가 - 단일 추가
     * 링크 리스트가 없는 경우에는 리스트를 새로 생성하여 추가
     *
     * @param link
     */
    public void addLinks(ApprovalLink link) {
        // 링크 리스트가 없을 경우
        if (this.linkList == null || this.linkList.isEmpty()) {
            this.linkList = List.of(link);
            return;
        }

        this.linkList.add(link);
    }
}
