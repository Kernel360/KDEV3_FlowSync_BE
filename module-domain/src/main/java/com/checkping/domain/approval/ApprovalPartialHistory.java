package com.checkping.domain.approval;

import com.checkping.domain.BaseEntity;
import com.checkping.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Table(name = "approval_partial_history")
@Entity
public class ApprovalPartialHistory extends BaseEntity {
    /*
    id : id
    approval : 결재(FK : approval_id)
    updated_name : 변경 항목 이름
    updated_value : 변경 값
    previous_value : 변경 이전 값
    updated_at : 수정 일시
    updater : 수정자(FK : updater_id)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_id", nullable = false)
    private Approval approval;

    @Column(name = "updated_name", nullable = false, length = 100)
    private String updatedName;

    @Column(name = "updated_value", nullable = false,length = 500)
    private String updatedValue;

    @Column(name = "previous_value", nullable = false,length = 500)
    private String previousValue;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater_id", nullable = false)
    private Member updater;
}
