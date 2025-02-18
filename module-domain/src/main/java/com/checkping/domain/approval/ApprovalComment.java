package com.checkping.domain.approval;

import com.checkping.domain.BaseEntity;
import com.checkping.domain.member.Member;
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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Table(name = "approval_comment")
@Entity
public class ApprovalComment extends BaseEntity {
    /*
    id: id
    approval : 결재(FK : approval_id)
    content : 댓글 내용
    reg_at : 작성 일시
    updated_at : 수정 일시
    deleted_yn : 삭제 여부
    parent : 부모 댓글
    register : 댓글 작성자
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_id", nullable = false)
    private Approval approval;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @CreatedDate
    @Column(name = "reg_at")
    private LocalDateTime regAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "deleted_yn", nullable = false)
    private DeleteStatus deleteYn;

    @ManyToOne(fetch = FetchType.LAZY)
    private ApprovalComment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "register_id")
    private Member register;

    @Getter
    @RequiredArgsConstructor
    public enum DeleteStatus {
        Y("비활성화"), N("활성화");
        private final String description;
    }

    /**
     * 댓글 생성 팩토리 메서드
     *
     * @param content  댓글 내용
     * @param approval 결재 Entity
     * @return 댓글 Entity
     */
    public static ApprovalComment generate(String content, Approval approval, Member register) {
        ApprovalComment approvalComment = new ApprovalComment();
        approvalComment.content = content;
        approvalComment.approval = approval;
        approvalComment.register = register;

        approvalComment.activate();

        return approvalComment;
    }

    /**
     * 부모 댓글이 있는 경우 생성 팩토리 메서드
     *
     * @param content  댓글 내용
     * @param approval 결재 Entity
     * @param parent   부모 댓글 Entity
     * @return 댓글 Entity
     */
    public static ApprovalComment generate(String content, Approval approval,
        ApprovalComment parent, Member register) {
        ApprovalComment approvalComment = generate(content, approval, register);
        approvalComment.parent = parent;

        return approvalComment;
    }

    /**
     * 댓글 비활성화
     */
    public void deactivate() {
        this.deleteYn = DeleteStatus.Y;
    }

    /**
     * 댓글 활성화
     */
    public void activate() {
        this.deleteYn = DeleteStatus.N;
    }

    /**
     * 댓글 내용 수정
     *
     * @param content 수정할 댓글 내용
     */
    public void updateContent(String content) {
        this.content = content;
    }

    /**
     * 댓글 삭제 여부 확인
     *
     * @return  삭제 여부
     */
    public boolean isDeleted() {
        return this.deleteYn == DeleteStatus.Y;
    }
}
