package com.checkping.dto;

import com.checkping.domain.board.TaskBoard;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class TaskBoardResponse {

    @Getter
    @Setter
    @ToString
    public static class TaskBoardDto {
        /*
        id : 게시글 고유 ID
        number : 게시글 번호
        title : 게시글 제목
        content : 게시글 본문 내용
        regAt : 게시글 작성 일시
        editAt : 게시글 마지막 수정 일시
        approverAt : 게시글 승인 일시
        boardCategory : 게시글 카테고리 (enum, String)
        boardStatus : 게시글 상태 (enum, String)
        deletedYn : 게시글 삭제 여부 ('Y' 또는 'N')
         */
        private Long id;
        private Integer number;
        private String title;
        private String content;
        private LocalDateTime regAt;
        private LocalDateTime editAt;
        private LocalDateTime approverAt;
        private TaskBoard.BoardCategory boardCategory;
        private TaskBoard.BoardStatus boardStatus;
        private TaskBoard.DeleteStatus deletedYn;

        public static TaskBoardDto toDto(TaskBoard taskBoard) {
            TaskBoardDto boardDto = new TaskBoardDto();
            boardDto.setId(taskBoard.getId());
            boardDto.setNumber(taskBoard.getNumber());
            boardDto.setTitle(taskBoard.getTitle());
            boardDto.setContent(taskBoard.getContent());
            boardDto.setRegAt(taskBoard.getRegAt());
            boardDto.setEditAt(taskBoard.getEditAt());
            boardDto.setApproverAt(taskBoard.getApproverAt());
            boardDto.setBoardCategory(taskBoard.getBoardCategory());
            boardDto.setBoardStatus(taskBoard.getBoardStatus());
            return boardDto;
        }
    }
}
