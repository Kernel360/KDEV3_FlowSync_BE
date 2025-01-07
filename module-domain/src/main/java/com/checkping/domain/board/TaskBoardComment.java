package com.checkping.domain.board;

import com.checkping.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@ToString
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
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content")
    private String content;

    @CreatedDate
    @Column(name = "reg_at", nullable = false)
    private LocalDateTime regAt;

    @LastModifiedDate
    @Column(name = "edit_at", nullable = false)
    private LocalDateTime editAt;

    @Column(name = "parent_id", nullable = false)
    private Long parentId;

    @Column(name = "deleted_yn", nullable = false)
    private Character deletedYn;
}
