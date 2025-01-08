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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_board")
@Entity
public class TaskBoard extends BaseEntity {
    /*
    id : id
    number : 게시글 번호
    title : 게시글 제목
    content : 게시글 본문
    regAt : 작성 일시
    editAt : 수정 일시
    approverAt : 승인 일시
    boardCategory : 게시글 유형
    boardStatus : 게시글 상태
    deletedYn : 삭제 여부
    parent : 부모 게시글
    replyList : 답글 리스트
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "number")
    private Integer number;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @CreatedDate
    @Column(name = "reg_at", nullable = false)
    private LocalDateTime regAt;

    @LastModifiedDate
    @Column(name = "edit_at", nullable = false)
    private LocalDateTime editAt;

    @Column(name = "approver_at")
    private LocalDateTime approverAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_category", nullable = false)
    private BoardCategory boardCategory;

    @Column(name = "board_status", nullable = false)
    private BoardStatus boardStatus;

    @Column(name = "deleted_yn", nullable = false)
    private Character deletedYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private TaskBoard parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<TaskBoard> replyList = new ArrayList<>();

    @Getter
    @RequiredArgsConstructor
    public enum BoardCategory {
        QUESTION("질문"), REQUEST("요청"), ANSWER("답변");
        private final String description;
    }

    @Getter
    @RequiredArgsConstructor
    public enum BoardStatus {
        PROGRESS("진행중"), COMPLETED("완료"), SUSPENSION("보류"), PERMISSION_REQUEST("승인 요청");
        private final String description;
    }
}
