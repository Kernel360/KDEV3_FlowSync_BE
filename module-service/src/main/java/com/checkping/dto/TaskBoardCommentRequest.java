package com.checkping.dto;

import com.checkping.domain.board.TaskBoard;
import com.checkping.domain.board.TaskBoardComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskBoardCommentRequest {

    @Getter
    @ToString
    public static class RegisterDto {
        /*
        content : 댓글 내용
        taskBoardId : taskBoard 의 id(pk)
         */
        @Schema(description = "게시글 댓글 내용")
        private String content;

        /**
         * RegisterDto -> Entity
         *
         * @param registerDto 등록 정보
         * @param taskBoard taskBoard (조회한 Entity)
         * @return TaskBoardComment 엔티티
         */
        public static TaskBoardComment toEntity (RegisterDto registerDto, TaskBoard taskBoard) {
            return TaskBoardComment.builder()
                .content(registerDto.getContent())
                .taskBoard(taskBoard)
                .build();
        }
    }

    @Getter
    @ToString
    public static class UpdateDto {
        /*
        content : 댓글 내용
         */
        @Schema(description = "게시글 댓글 내용")
        private String content;
    }
}
