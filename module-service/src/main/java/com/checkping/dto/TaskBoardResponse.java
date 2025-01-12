package com.checkping.dto;

import com.checkping.domain.board.TaskBoard;
import com.checkping.dto.TaskBoardResponse.TaskBoardListDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskBoardResponse {

    @Getter
    @Setter
    @ToString
    public static class TaskBoardListDto {
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

        public static TaskBoardListDto toDto(TaskBoard taskBoard) {
            TaskBoardListDto boardDto = new TaskBoardListDto();
            boardDto.setId(taskBoard.getId());
            boardDto.setNumber(taskBoard.getNumber());
            boardDto.setTitle(taskBoard.getTitle());
            boardDto.setContent(taskBoard.getContent());
            boardDto.setRegAt(taskBoard.getRegAt());
            boardDto.setEditAt(taskBoard.getEditAt());
            boardDto.setApproverAt(taskBoard.getApproverAt());
            boardDto.setBoardCategory(taskBoard.getBoardCategory());
            boardDto.setBoardStatus(taskBoard.getBoardStatus());
            boardDto.setDeletedYn(taskBoard.getDeletedYn());
            return boardDto;
        }
    }

    @Getter
    @Setter
    @ToString
    public static class TaskBoardItemDto {

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
        commentList : 게시글 댓글 리스트
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
        private List<TaskBoardCommentResponse.TaskBoardCommentDto> commentList;

        public static TaskBoardResponse.TaskBoardItemDto toDto(TaskBoard taskBoard) {
            TaskBoardResponse.TaskBoardItemDto boardDto = new TaskBoardResponse.TaskBoardItemDto();
            boardDto.setId(taskBoard.getId());
            boardDto.setNumber(taskBoard.getNumber());
            boardDto.setTitle(taskBoard.getTitle());
            boardDto.setContent(taskBoard.getContent());
            boardDto.setRegAt(taskBoard.getRegAt());
            boardDto.setEditAt(taskBoard.getEditAt());
            boardDto.setApproverAt(taskBoard.getApproverAt());
            boardDto.setBoardCategory(taskBoard.getBoardCategory());
            boardDto.setBoardStatus(taskBoard.getBoardStatus());
            boardDto.setDeletedYn(taskBoard.getDeletedYn());

            // Entity -> Dto
            List<TaskBoardCommentResponse.TaskBoardCommentDto> comments =
                TaskBoardCommentResponse.TaskBoardCommentDto.toDtoList(taskBoard.getCommentList());
            boardDto.setCommentList(comments);

            return boardDto;
        }
    }
}
