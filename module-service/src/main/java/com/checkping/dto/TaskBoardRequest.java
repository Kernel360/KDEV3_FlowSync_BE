package com.checkping.dto;

import com.checkping.domain.board.TaskBoard;
import com.checkping.exception.project.TaskBoardInvalidBoardCategoryException;
import com.checkping.exception.project.TaskBoardInvalidBoardStatusException;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
        boardCategory : 게시글 카테고리 (enum, String
        boardStatus : 게시글 상태 (enum, String)
        taskBoardLinkList : 첨부 링크 리스트 (List<TaskBoardLinkRequest.RegisterDto>)
         */
        @Schema(description = "게시글 제목", example = "게시글 제목 입니다.")
        private String title;
        @Schema(description = "게시글 본문", example = "게시글 본문 입니다.")
        private String content;
        @Schema(description = "게시글 유형")
        private String boardCategory;
        @Schema(description = "게시글 상태")
        private String boardStatus;
        @Schema(description = "게시글 첨부 링크 목록")
        private List<TaskBoardLinkRequest.RegisterDto> taskBoardLinkList;

        /**
         * 업무 관리 게시글 등록 요청 정보로 업무 관리 게시글 엔티티를 만드는 메서드
         *
         * @param registerDto 엄무 관리 게시글 등록 요청 정보
         * @return TaskBoard Entity
         */
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
        keyword : 게시글 검색어 (String)
         */
        @Schema(description = "게시글 유형")
        private final TaskBoard.BoardCategory boardCategory;
        @Schema(description = "게시글 상태")
        private final TaskBoard.BoardStatus boardStatus;
        @Schema(description = "게시글 검색어")
        private final String keyword;

        /**
         * String 으로 들어온 값을 Enum 으로 변경한다.
         *
         * @param boardCategory RequestParam 으로 받아온 TaskBoard.BoardCategory 로 변경할 문자열
         * @param boardStatus   RequestParam 으로 받아온 TaskBoard.BoardStatus 로 변경할 문자열
         * @param keyword       검색어
         */
        public SearchCondition(String boardCategory, String boardStatus, String keyword) {

            // String -> Enum
            this.boardCategory = StringUtils.hasText(boardCategory) ? TaskBoardRequest.convertBoardCategory(boardCategory) : null;

            // String -> Enum
            this.boardStatus = StringUtils.hasText(boardStatus) ? TaskBoardRequest.convertBoardStatus(boardStatus) : null;

            // 검색어
            this.keyword = StringUtils.hasText(keyword) ? keyword : null;
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
     * @deprecated 해당 메서드는 Enum 을 위한 Dto 를 생성하는 것에 따라 폐기할 예정입니다.
     * TaskBoardStatus.Request.toEntity())
     *
     * @param value BoardCategory 로 변환할 문자열
     * @return TaskBoard.BoardCategory
     *
     */
    @Deprecated
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
