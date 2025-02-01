package com.checkping.domain.question;

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
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@ToString(exclude = "question")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "question_comment")
@Entity
public class QuestionComment extends BaseEntity {

    /*
    id : 업무 관리 게시글 댓글 아이디
    content : 댓글 내용
    regAt : 작성 일시
    editAt : 수정 일시
    parentId : 부모 댓글 아이디
    deletedYn : 삭제 여부
    question : 업무 관리 게시글 (join)
    parent : 부모 댓글 (join)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @CreatedDate
    @Column(name = "reg_at", nullable = false)
    private LocalDateTime regAt;

    @LastModifiedDate
    @Column(name = "edit_at", nullable = false)
    private LocalDateTime editAt;

    @Column(name = "deleted_yn", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeleteStatus deletedYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private QuestionComment parent;

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
     * @param question question (조회한 Entity)
     * @return QuestionComment 엔티티
     */
    public static QuestionComment generate(String content, Question question) {
        QuestionComment questionComment = new QuestionComment();
        questionComment.content = content;
        questionComment.question = question;
        questionComment.activate();
        return questionComment;
    }

    /**
     * 댓글 생성 팩토리 메서드
     *
     * @param content  댓글 내용
     * @param question question (조회한 Entity)
     * @param parent   부모 댓글
     * @return QuestionComment 엔티티
     */
    public static QuestionComment generate(String content, Question question,
        QuestionComment parent) {
        QuestionComment questionComment = generate(content, question);
        // 부모 댓글 추가
        questionComment.parent = parent;
        return questionComment;
    }

    // soft delete 적용 = 게시글 비활성화
    public void deactivate() {
        this.deletedYn = DeleteStatus.Y;
    }

    // soft delete 해제 = 게시글 활성화
    public void activate() {
        this.deletedYn = DeleteStatus.N;
    }

    // update
    public void update(String content) {
        this.content = content;
    }
}
