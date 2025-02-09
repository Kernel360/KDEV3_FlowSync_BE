package com.checkping.domain.approval;

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
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Table(name = "approval_link")
@Entity
public class ApprovalLink extends BaseEntity {

    /*
    id : id
    approval : 결재(FK : approval_id)
    link_name : 링크 이름
    link_url : 링크 url
    is_deleted : 삭제 여부
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_id")
    private Approval approval;

    @Column(name = "link_name")
    private String linkName;

    @Column(name = "link_url")
    private String linkUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "deleted_yn")
    private ApprovalFile.DeleteStatus deletedYn;

    @Getter
    @RequiredArgsConstructor
    public enum DeleteStatus {
        Y("비활성화"), N("활성화");
        private final String description;
    }

    /**
     * ApprovalLink 생성 메서드(정적 팩토리 메서드)
     *
     * @param approval 결재
     * @param linkName 링크 이름
     * @param linkUrl  링크 url
     * @return ApprovalLink 인스턴스
     */
    public static ApprovalLink generate(Approval approval, String linkName, String linkUrl) {
        ApprovalLink approvalLink = new ApprovalLink();
        approvalLink.approval = approval;
        approvalLink.linkName = linkName;
        approvalLink.linkUrl = linkUrl;

        approvalLink.activate();

        return approvalLink;
    }

    // soft delete 적용 = 게시글 비활성화
    public void deactivate() {
        this.deletedYn = ApprovalFile.DeleteStatus.Y;
    }

    // soft delete 해제 = 게시글 활성화
    public void activate() {
        this.deletedYn = ApprovalFile.DeleteStatus.N;
    }
}
