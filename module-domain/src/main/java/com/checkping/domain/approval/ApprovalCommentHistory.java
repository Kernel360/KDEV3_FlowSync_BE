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
public class ApprovalCommentHistory extends BaseEntity {
    /*
    id : id
    approvalComment : 결재(FK : approval_comment_id)
    content : 댓글 내용
    updated_at : 수정 일시
    updater : 수정자(FK : updater_id)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_comment_id", nullable = false)
    private ApprovalComment approvalComment;

    @Column(name = "content", length = 500)
    private String content;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater_id", nullable = false)
    private Member updater;
}
