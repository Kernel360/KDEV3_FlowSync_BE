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
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "save_name")
    private String saveName;

    @Column(name = "url")
    private String url;

    @Column(name = "size")
    private long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_board_id")
    private TaskBoard taskBoard;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TaskBoardFile that)) {
            return false;
        }
        return size == that.size && Objects.equals(id, that.id) && Objects.equals(
            url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, size);
    }
}