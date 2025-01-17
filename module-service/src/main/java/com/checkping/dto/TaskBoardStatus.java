package com.checkping.dto;

import com.checkping.domain.board.TaskBoard;
import com.checkping.domain.board.TaskBoard.BoardCategory;
import com.checkping.exception.project.TaskBoardInvalidBoardCategoryException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskBoardStatus {

    @Getter
    public static class Request {

        /*
        label : Enum 한글 설명
        value : Enum 값(DB)
        */
        @Schema(description = "Enum 이름")
        private String label;
        @Schema(description = "Enum 설명")
        private String value;

        /**
         * Request Dto -> Entity
         *
         * @param request 게시글 상태 Dto
         * @return TaskBoard.BoardCategory
         */
        public static TaskBoard.BoardStatus toEntity(Request request) {
            try {
                // label(String) -> Enum
                return TaskBoard.BoardStatus.valueOf(request.getValue().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new TaskBoardInvalidBoardCategoryException(request.toString());
            }
        }
    }

    @Getter
    public static class Response {

        /*
        label : Enum 한글 설명
        value : Enum 값(DB)
        */
        @Schema(description = "Enum 이름")
        private String label;
        @Schema(description = "Enum 설명")
        private String value;

        public static Response toDto(BoardCategory boardCategory) {
            Response dto = new Response();
            dto.label = boardCategory.name();
            dto.value = boardCategory.getDescription();
            return dto;
        }
    }
}
