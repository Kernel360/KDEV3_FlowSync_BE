package com.checkping.dto;

import com.checkping.common.utils.FileResponse;
import com.checkping.domain.board.TaskBoard;
import com.checkping.dto.TaskBoardResponse.TaskBoardListDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
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
        @Schema(description = "게시글 번호")
        private Integer number;
        @Schema(description = "게시글 제목", example = "게시글 제목 입니다.")
        private String title;
        @Schema(description = "게시글 본문", example = "게시글 본문 입니다.")
        private String content;
        @Schema(description = "등록 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime regAt;
        @Schema(description = "수정 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime editAt;
        @Schema(description = "승인 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime approverAt;
        @Schema(description = "게시글 유형")
        private TaskBoard.BoardCategory boardCategory;
        @Schema(description = "게시글 상태")
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
        @Schema(description = "게시글 번호")
        private Integer number;
        @Schema(description = "게시글 제목", example = "게시글 제목 입니다.")
        private String title;
        @Schema(description = "게시글 본문", example = "게시글 본문 입니다.")
        private String content;
        @Schema(description = "등록 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime regAt;
        @Schema(description = "수정 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime editAt;
        @Schema(description = "승인 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime approverAt;
        @Schema(description = "게시글 유형")
        private TaskBoard.BoardCategory boardCategory;
        @Schema(description = "게시글 상태")
        private TaskBoard.BoardStatus boardStatus;
        private TaskBoard.DeleteStatus deletedYn;
        @Schema(description = "게시글 댓글 목록")
        private List<TaskBoardCommentResponse.TaskBoardCommentDto> commentList;
        @Schema(description = "게시글 첨부 링크 목록")
        private List<TaskBoardLinkResponse.TaskBoardLinkDto> taskBoardLinkList;
        @Schema(description = "게시글 첨부 파일 목록")
        private List<FileResponse> taskBoardFileList;

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

            // Entity -> Dto (TaskBoardComment)
            List<TaskBoardCommentResponse.TaskBoardCommentDto> comments =
                TaskBoardCommentResponse.TaskBoardCommentDto.toDtoList(taskBoard.getCommentList());
            boardDto.setCommentList(comments);

            // Entity -> Dto (TaskBoardLink)
            List<TaskBoardLinkResponse.TaskBoardLinkDto> links = TaskBoardLinkResponse.TaskBoardLinkDto.toDtoList(
                taskBoard.getTaskBoardLinkList());
            boardDto.setTaskBoardLinkList(links);

            // Entity -> Dto (TaskBoardFile)
            List<FileResponse> fileList = FileResponse.toDtoList(taskBoard.getTaskBoardFileList());
            boardDto.setTaskBoardFileList(fileList);

            return boardDto;
        }
    }
}
