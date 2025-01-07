package com.checkping.domain.board;

import com.checkping.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "task_board_comment")
@Entity
public class TaskBoardLink extends BaseEntity {
    /*
    id : 업무 관리 게시글 첨부 링크 아이디
    name : 업무 관리 게시글 첨부 링크 이름
    url : 업무 관리 게시글 첨부 링크 URL
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "url", nullable = false)
    private String url;
}
