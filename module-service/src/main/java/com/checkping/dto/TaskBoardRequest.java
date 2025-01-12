package com.checkping.dto;

import com.checkping.domain.board.TaskBoard;
import com.checkping.exception.project.TaskBoardInvalidBoardCategoryException;
import com.checkping.exception.project.TaskBoardInvalidBoardStatusException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskBoardRequest {

    @Getter
    @ToString
    public static class RegisterDto {
        /*
        title : 게시글 제목
        content : 게시글 본문 내용
        boardCategory : 게시글 카테고리 (enum, String)
        boardStatus : 게시글 상태 (enum, String)
         */
        private String title;
        private String content;
        private String boardCategory;
        private String boardStatus;

        public static TaskBoard toEntity(RegisterDto registerDto) {
            return TaskBoard.builder()
                .title(registerDto.getTitle())
                .content(registerDto.getContent())
                .boardCategory(TaskBoardRequest.convertBoardCategory(registerDto.getBoardCategory()))
                .boardStatus(TaskBoardRequest.convertBoardStatus(registerDto.getBoardStatus()))
                .build();
        }
    }

    @Getter
    @ToString
    public static class SearchCondition {
        /*
        boardCategory : 게시글 카테고리 (enum, String)
        boardStatus : 게시글 상태 (enum, String)
         */
        private final TaskBoard.BoardCategory boardCategory;
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
     */
    public static TaskBoard.BoardStatus convertBoardStatus(String value) {
        try {
            return TaskBoard.BoardStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TaskBoardInvalidBoardStatusException(value);
        }
    }
}
