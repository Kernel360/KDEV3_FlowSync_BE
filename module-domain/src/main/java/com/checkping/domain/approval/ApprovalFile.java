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
@Table(name = "approval_file")
@Entity
public class ApprovalFile extends BaseEntity {

    /*
    id: id
    approval : 결재(FK : approval_id)
    original_name : 첨부 파일 원본 명
    save_name : 첨부 파일 저장 명
    url : 첨부 파일 URL
    size : 첨부 파일 용량
    is_deleted : 삭제 여부
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_id")
    private Approval approval;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "save_name")
    private String saveName;

    @Column(name = "url")
    private String url;

    @Column(name = "size")
    private long size;

    @Enumerated(EnumType.STRING)
    @Column(name = "deleted_yn")
    private DeleteStatus deletedYn;

    @Getter
    @RequiredArgsConstructor
    public enum DeleteStatus {
        Y("비활성화"), N("활성화");
        private final String description;
    }

    public static ApprovalFile generate(Approval approval, String originalName, String saveName,
        String url, long size) {
        ApprovalFile approvalFile = new ApprovalFile();
        approvalFile.approval = approval;
        approvalFile.originalName = originalName;
        approvalFile.saveName = saveName;
        approvalFile.url = url;
        approvalFile.size = size;

        approvalFile.activate();

        return approvalFile;
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
