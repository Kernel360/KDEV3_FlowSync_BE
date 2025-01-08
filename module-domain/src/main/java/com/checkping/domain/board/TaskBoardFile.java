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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_board_file")
@Entity
public class TaskBoardFile extends BaseEntity {
    /*
    id : 업무 관리 게시글 첨부 파일 아이디
    name : 업무 관리 게시글 첨부 파일 명
    url : 업무 관리 게시글 첨부 파일 url
    size : 업무 관리 게시글 첨부 용량
    taskBoard : 업무 관리 게시글 (join)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "size", nullable = false)
    private String size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_board_id", nullable = false)
    private TaskBoard taskBoard;
}
