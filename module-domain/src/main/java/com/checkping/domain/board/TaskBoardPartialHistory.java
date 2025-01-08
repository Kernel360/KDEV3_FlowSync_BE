package com.checkping.domain.board;

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
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_board_partial_history")
@Entity
public class TaskBoardPartialHistory extends BaseEntity {
    /*
    id : 업무 관리 게시글 부분이력 ID
    updateName : 변경 항목 이름
    currentValue : 현재 값
    previousValue : 변경 이전 값
    updatedAt : 수정 일시
    taskBoard : 업무 관리 게시글 (join)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "update_name", nullable = false, length = 100)
    private String updateName;

    @Column(name = "current_value", nullable = false, length = 5000)
    private String currentValue;

    @Column(name = "previous_value", nullable = false, length = 5000)
    private String previousValue;

    @LastModifiedDate
    @Column (name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_board_id", nullable = false)
    private TaskBoard taskBoard;
}
