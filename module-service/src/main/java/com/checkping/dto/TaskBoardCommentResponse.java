package com.checkping.dto;

import com.checkping.domain.board.TaskBoardComment;
import com.checkping.domain.board.TaskBoardComment.DeleteStatus;
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

        private Long taskBoardCommentId;
        private String content;
        private LocalDateTime regAt;
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
