package com.checkping.domain.approval;

import com.checkping.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

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
    private String size;

    public static ApprovalFile generate(Approval approval, String originalName, String saveName, String url, String size) {
        ApprovalFile approvalFile = new ApprovalFile();
        approvalFile.approval = approval;
        approvalFile.originalName = originalName;
        approvalFile.saveName = saveName;
        approvalFile.url = url;
        approvalFile.size = size;
        return approvalFile;
    }
}
