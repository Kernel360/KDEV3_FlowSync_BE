package com.checkping.dto.question.comment;


import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionCommentRegister {

    @Getter
    public static class Request {
        /*
        content : 댓글 내용
         */
        @Schema(description = "질문 게시글 댓글 내용")
        private String content;

        /**
         * Request -> Entity
         *
         * @param question  질문 게시글 Entity
         * @param request   등록 정보
         * @return  QuestionComment 엔티티
         */
        public static QuestionComment toEntity(Question question, Request request) {
            return QuestionComment.generate(request.getContent(), question);
        }
    }

}
