package com.checkping.domain.board;

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
@ToString(exclude = "taskBoard")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_board_comment")
@Entity
public class TaskBoardComment extends BaseEntity {
    /*
    id : 업무 관리 게시글 댓글 아이디
    content : 댓글 내용
    regAt : 작성 일시
    editAt : 수정 일시
    parentId : 부모 댓글 아이디
    deletedYn : 삭제 여부
    taskBoard : 업무 관리 게시글 (join)
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

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "deleted_yn", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeleteStatus deletedYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_board_id", nullable = false)
    private TaskBoard taskBoard;

    @Getter
    @RequiredArgsConstructor
    public enum DeleteStatus {
        Y("비활성화"), N("활성화");
        private final String description;
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
