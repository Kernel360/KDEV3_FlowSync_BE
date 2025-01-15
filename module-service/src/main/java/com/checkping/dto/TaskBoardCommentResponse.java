package com.checkping.dto;

import com.checkping.domain.board.TaskBoardComment;
import com.checkping.domain.board.TaskBoardComment.DeleteStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskBoardCommentResponse {

    @Getter
    @ToString
    public static class TaskBoardCommentDto {

        @Schema(description = "게시글 댓글 ID")
        private Long taskBoardCommentId;
        @Schema(description = "게시글 댓글 내용")
        private String content;
        @Schema(description = "게시글 댓글 등록 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime regAt;
        @Schema(description = "게시글 댓글 수정 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime editAt;
        private DeleteStatus deletedYn;


        /**
         * Entity -> Dto
         *
         * @param taskBoardComment TaskBoardComment Entity
         * @return TaskBoardCommentDto
         */
        public static TaskBoardCommentDto toDto(TaskBoardComment taskBoardComment) {
            TaskBoardCommentDto dto = new TaskBoardCommentDto();
            dto.taskBoardCommentId = taskBoardComment.getId();
            dto.content = taskBoardComment.getContent();
            dto.regAt = taskBoardComment.getRegAt();
            dto.editAt = taskBoardComment.getEditAt();
            dto.deletedYn = taskBoardComment.getDeletedYn();
            return dto;
        }

        /**
         * TaskBoardComment 리스트를 TaskBoardCommentDto 리스트로 변환
         *
         * @param taskBoardCommentList TaskBoardComment 의 리스트
         * @return TaskBoardCommentDto 리스트
         */
        public static List<TaskBoardCommentDto> toDtoList(
            List<TaskBoardComment> taskBoardCommentList) {

            // null check
            if (taskBoardCommentList == null) {
                return Collections.emptyList();
            }

            return taskBoardCommentList.stream().map(TaskBoardCommentDto::toDto).toList();
        }

    }
}
