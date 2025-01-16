package com.checkping.dto;

import com.checkping.common.utils.FileResponse;
import com.checkping.domain.board.TaskBoard;
import com.checkping.dto.TaskBoardCommentResponse.TaskBoardCommentDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskBoardGetItem {

    @Getter
    @ToString
    public static class Response {
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
        private TaskBoardCategoryInfo.Response boardCategory;
        @Schema(description = "게시글 상태")
        private TaskBoardStatusInfo.Response boardStatus;
        private TaskBoard.DeleteStatus deletedYn;
        @Schema(description = "게시글 댓글 목록")
        private List<TaskBoardCommentDto> commentList;
        @Schema(description = "게시글 첨부 링크 목록")
        private List<TaskBoardLinkResponse.TaskBoardLinkDto> taskBoardLinkList;
        @Schema(description = "게시글 첨부 파일 목록")
        private List<FileResponse> taskBoardFileList;

        /**
         * TaskBoard(Entity) 를 TaskBoardGetItem.Response(Dto) 변환
         *
         * @param taskBoard 업무 관리 게시글 Entity
         * @return TaskBoardGetItem.Response(Dto)
         */
        public static TaskBoardGetItem.Response toDto(TaskBoard taskBoard) {

            TaskBoardGetItem.Response dto = new TaskBoardGetItem.Response();
            dto.id = taskBoard.getId();
            dto.number = taskBoard.getNumber();
            dto.title = taskBoard.getTitle();
            dto.content = taskBoard.getContent();
            dto.regAt = taskBoard.getRegAt();
            dto.editAt = taskBoard.getEditAt();
            dto.approverAt = taskBoard.getApproverAt();
            dto.boardCategory = TaskBoardCategoryInfo.Response.toDto(taskBoard.getBoardCategory());
            dto.boardStatus = TaskBoardStatusInfo.Response.toDto(taskBoard.getBoardStatus());
            dto.deletedYn = taskBoard.getDeletedYn();

            // Entity -> Dto (TaskBoardComment)
            dto.commentList = TaskBoardCommentResponse.TaskBoardCommentDto.toDtoList(
                taskBoard.getCommentList());

            // Entity -> Dto (TaskBoardLink)
            dto.taskBoardLinkList = TaskBoardLinkResponse.TaskBoardLinkDto.toDtoList(
                taskBoard.getTaskBoardLinkList());

            // Entity -> Dto (TaskBoardFile)
            dto.taskBoardFileList = FileResponse.toDtoList(taskBoard.getTaskBoardFileList());

            return dto;
        }
    }
}
