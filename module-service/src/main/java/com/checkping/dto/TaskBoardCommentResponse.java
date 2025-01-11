package com.checkping.dto;

import com.checkping.domain.board.TaskBoardComment;
import com.checkping.domain.board.TaskBoardComment.DeleteStatus;
import java.time.LocalDateTime;
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
        private DeleteStatus deleteStatus;


        /**
         * Entity -> Dto
         *
         * @param taskBoardComment TaskBoardComment Entity
         * @return TaskBoardCommentDto
         */
        public static TaskBoardCommentDto toDto (TaskBoardComment taskBoardComment) {
            TaskBoardCommentDto dto = new TaskBoardCommentDto();
            dto.taskBoardCommentId = taskBoardComment.getId();
            dto.content = taskBoardComment.getContent();
            dto.regAt = taskBoardComment.getRegAt();
            dto.editAt = taskBoardComment.getEditAt();
            return dto;
        }

    }
}
