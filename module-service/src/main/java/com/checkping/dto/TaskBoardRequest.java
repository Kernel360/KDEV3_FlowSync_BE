package com.checkping.dto;

import com.checkping.domain.board.TaskBoard;
import com.checkping.exception.project.TaskBoardInvalidBoardCategoryException;
import com.checkping.exception.project.TaskBoardInvalidBoardStatusException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskBoardRequest {

    @Getter
    @ToString
    public static class SearchCondition {
        /*
        boardCategory : 게시글 카테고리 (enum, String)
        boardStatus : 게시글 상태 (enum, String)
         */
        @Schema(description = "게시글 유형")
        private final TaskBoard.BoardCategory boardCategory;
        @Schema(description = "게시글 상태")
        private final TaskBoard.BoardStatus boardStatus;

        /**
         * String 으로 들어온 값을 Enum 으로 변경한다.
         *
         * @param boardCategory RequestParam 으로 받아온 TaskBoard.BoardCategory 로 변경할 문자열
         * @param boardStatus RequestParam 으로 받아온 TaskBoard.BoardStatus 로 변경할 문자열
         */
        public SearchCondition(String boardCategory, String boardStatus) {

            // String -> Enum
            this.boardCategory = StringUtils.hasText(boardCategory) ? TaskBoardRequest.convertBoardCategory(boardCategory) : null;

            // String -> Enum
            this.boardStatus = StringUtils.hasText(boardStatus) ? TaskBoardRequest.convertBoardStatus(boardStatus) : null;
        }
    }

    @Getter
    @ToString
    public static class UpdateDto {
        /*
        title : 게시글 제목
        content : 게시글 본문
        boardCategory : 게시글 유형
        boardStatus : 게시글 상태
         */
        @Schema(description = "게시글 제목", example = "게시글 제목 입니다.")
        private String title;
        @Schema(description = "게시글 본문", example = "게시글 본문 입니다.")
        private String content;
        @Schema(description = "게시글 유형")
        private String boardCategory;
        @Schema(description = "게시글 상태")
        private String boardStatus;
    }

    /**
     * Enum : BoardCategory 변환 함수
     *
     * @param value BoardCategory 로 변환할 문자열
     * @return TaskBoard.BoardCategory
     */
    public static TaskBoard.BoardCategory convertBoardCategory(String value) {
        try {
            return TaskBoard.BoardCategory.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TaskBoardInvalidBoardCategoryException(value);
        }
    }

    /**
     * Enum : BoardStatus 변환 함수
     *
     * @param value BoardStatus 로 변환할 문자열
     * @return TaskBoard.BoardStatus
     *
     */
    public static TaskBoard.BoardStatus convertBoardStatus(String value) {
        try {
            return TaskBoard.BoardStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TaskBoardInvalidBoardStatusException(value);
        }
    }
}
