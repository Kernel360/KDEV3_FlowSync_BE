package com.checkping.dto;

import com.checkping.domain.board.TaskBoard;
import com.checkping.domain.board.TaskBoardLink;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskBoardLinkRequest {

    @Getter
    @ToString
    public static class RegisterDto {

        @Schema(description = "게시글 링크 이름")
        private String name;
        @Schema(description = "게시글 링크 url")
        private String url;

        /**
         * TaskBoardLink 등록 요청 Dto -> TaskBoardLink Entity
         *
         * @param taskBoard TaskBoard Entity
         * @param request Register Dto
         * @return TaskBoardLink 엔티티
         */
        public static TaskBoardLink toEntity(TaskBoard taskBoard, RegisterDto request) {
            return TaskBoardLink.builder()
                .name(request.getName())
                .url(request.getUrl())
                .taskBoard(taskBoard)
                .build();
        }
    }

}
