package com.checkping.dto.question.link;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionLink;
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
         * QuestionLink 등록 요청 Dto -> QuestionLink Entity
         *
         * @param question Question Entity
         * @param request Register Dto
         * @return QuestionLink 엔티티
         */
        public static QuestionLink toEntity(Question question, RegisterDto request) {
            return QuestionLink.builder()
                .name(request.getName())
                .url(request.getUrl())
                .question(question)
                .build();
        }
    }

}
