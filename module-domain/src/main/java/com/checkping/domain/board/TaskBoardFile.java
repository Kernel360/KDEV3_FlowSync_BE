package com.checkping.domain.board;

import com.checkping.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task_board_file")
@Entity
public class TaskBoardFile extends BaseEntity {
    /*
    id : id
    taskBoardId : 게시글 번호
    replyList : 답글 리스트
    originalName : 원본 파일명
    saveName : 저장 파일명
    url : 저장 URL
    size : 파일 Size
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_board_id")
    private Long taskBoardId;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "save_name")
    private String saveName;

    @Column(name = "url")
    private String url;

    @Column(name = "size")
    private long size;
}