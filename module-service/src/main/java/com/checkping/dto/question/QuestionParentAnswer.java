package com.checkping.dto.question;

import com.checkping.domain.question.Question;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionParentAnswer {

    public static class Response {
        /*
        id : 질문 게시글 답변 아이디
        title : 질문 게시글 제목
        category : 질문 게시글 카테고리
        status : 질문 게시글 상태
         */
        @Schema(description = "질문 게시글 답변 아이디")
        private Long id;
        @Schema(description = "질문 게시글 제목")
        private String title;
        @Schema(description = "질문 게시글 카테고리")
        private String category;
        @Schema(description = "질문 게시글 상태")
        private String status;

        public static Response toDto(Question question) {
            // check null
            if (question == null) {
                return null;
            }

            Response dto = new Response();
            dto.id = question.getId();
            dto.title = question.getTitle();
            dto.category = question.getCategory().name();
            dto.status = question.getStatus().name();
            return dto;
        }
    }
}
