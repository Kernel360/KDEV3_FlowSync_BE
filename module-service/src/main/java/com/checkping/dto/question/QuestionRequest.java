package com.checkping.dto.question;

import com.checkping.domain.question.Question;
import com.checkping.dto.question.link.TaskBoardLinkRequest;
import com.checkping.exception.question.QuestionCategoryException;
import com.checkping.exception.question.QuestionStatusException;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionRequest {

    @Getter
    @ToString
    public static class RegisterDto {
        /*
        title : 게시글 제목
        content : 게시글 본문 내용
        boardCategory : 게시글 카테고리 (enum, String
        boardStatus : 게시글 상태 (enum, String)
        questionLinkList : 첨부 링크 리스트 (List<TaskBoardLinkRequest.RegisterDto>)
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
         * @return Question Entity
         */
        public static Question toEntity(RegisterDto registerDto) {
            return Question.builder()
                .title(registerDto.getTitle())
                .content(registerDto.getContent())
                .boardCategory(QuestionRequest.convertBoardCategory(registerDto.getBoardCategory()))
                .boardStatus(QuestionRequest.convertBoardStatus(registerDto.getBoardStatus()))
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
        private final Question.BoardCategory boardCategory;
        @Schema(description = "게시글 상태")
        private final Question.BoardStatus boardStatus;
        @Schema(description = "게시글 검색어")
        private final String keyword;

        /**
         * String 으로 들어온 값을 Enum 으로 변경한다.
         *
         * @param boardCategory RequestParam 으로 받아온 Question.BoardCategory 로 변경할 문자열
         * @param boardStatus   RequestParam 으로 받아온 Question.BoardStatus 로 변경할 문자열
         * @param keyword       검색어
         */
        public SearchCondition(String boardCategory, String boardStatus, String keyword) {

            // String -> Enum
            this.boardCategory = StringUtils.hasText(boardCategory) ? QuestionRequest.convertBoardCategory(boardCategory) : null;

            // String -> Enum
            this.boardStatus = StringUtils.hasText(boardStatus) ? QuestionRequest.convertBoardStatus(boardStatus) : null;

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
     * @param value BoardCategory 로 변환할 문자열
     * @return Question.BoardCategory
     */
    public static Question.BoardCategory convertBoardCategory(String value) {
        try {
            return Question.BoardCategory.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new QuestionCategoryException(value);
        }
    }

    /**
     * Enum : BoardStatus 변환 함수
     *
     * @param value BoardStatus 로 변환할 문자열
     * @return Question.BoardStatus
     *
     */
    public static Question.BoardStatus convertBoardStatus(String value) {
        try {
            return Question.BoardStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new QuestionStatusException(value);
        }
    }
}
