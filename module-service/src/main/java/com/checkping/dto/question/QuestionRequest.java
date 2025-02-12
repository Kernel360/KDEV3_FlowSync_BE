package com.checkping.dto.question;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.Question.Category;
import com.checkping.domain.question.Question.Status;
import com.checkping.dto.question.link.QuestionLinkRequest;
import com.checkping.exception.question.QuestionCategoryException;
import com.checkping.exception.question.QuestionContentParsingException;
import com.checkping.exception.question.QuestionStatusException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        category : 게시글 카테고리 (enum, String
        status : 게시글 상태 (enum, String)
        linkList : 첨부 링크 리스트 (List<QuestionLinkRequest.RegisterDto>)
         */
        @Schema(description = "게시글 제목", example = "게시글 제목 입니다.")
        private String title;
        @Schema(description = "게시글 본문", example = "게시글 본문 입니다.")
        private String content;
        @Schema(description = "게시글 첨부 링크 목록")
        private List<QuestionLinkRequest.RegisterDto> linkList;

        /**
         * 업무 관리 게시글 등록 요청 정보로 업무 관리 게시글 엔티티를 만드는 메서드
         *
         * @param registerDto 엄무 관리 게시글 등록 요청 정보
         * @return Question Entity
         */
        public static Question toEntity(RegisterDto registerDto) {
            return Question.builder().title(registerDto.getTitle())
                .content(registerDto.getContent()).build();
        }
    }

    @Getter
    @ToString
    public static class SearchCondition {

        /*
        category : 게시글 카테고리 (enum, String)
        status : 게시글 상태 (enum, String)
        keyword : 게시글 검색어 (String)
         */
        @Schema(description = "게시글 유형")
        private final Category category;
        @Schema(description = "게시글 상태")
        private final Status status;
        @Schema(description = "게시글 검색어")
        private final String keyword;

        /**
         * String 으로 들어온 값을 Enum 으로 변경한다.
         *
         * @param category RequestParam 으로 받아온 Question.Category 로 변경할 문자열
         * @param status   RequestParam 으로 받아온 Question.Status 로 변경할 문자열
         * @param keyword  검색어
         */
        public SearchCondition(String category, String status, String keyword) {

            // String -> Enum
            this.category =
                StringUtils.hasText(category) ? QuestionRequest.convertCategory(category) : null;

            // String -> Enum
            this.status =
                StringUtils.hasText(status) ? QuestionRequest.convertStatus(status) : null;

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
        category : 게시글 유형
        status : 게시글 상태
         */
        @Schema(description = "게시글 제목", example = "게시글 제목 입니다.")
        private String title;
        @Schema(description = "게시글 본문", example = "게시글 본문 입니다.")
        private List<QuestionContent> content;
        @Schema(description = "게시글 유형")
        private String category;
        @Schema(description = "게시글 상태")
        private String status;

        public String getContent() {
            return toContentString(this.content);
        }

        /**
         * JSON LIST -> String
         *
         * @return content String
         */
        private String toContentString(List<QuestionContent> content) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(content);
            } catch (Exception e) {
                throw new QuestionContentParsingException();
            }
        }
    }

    /**
     * Enum : Category 변환 함수
     *
     * @param value Category 로 변환할 문자열
     * @return Question.Category
     */
    public static Category convertCategory(String value) {
        try {
            return Category.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new QuestionCategoryException(value);
        }
    }

    /**
     * Enum : Status 변환 함수
     *
     * @param value Status 로 변환할 문자열
     * @return Question.Status
     */
    public static Status convertStatus(String value) {
        try {
            return Status.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new QuestionStatusException(value);
        }
    }
}
