package com.checkping.dto.question.comment;

import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.member.Member;
import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionReCommentRegister {

    @Getter
    public static class Request {

        /*
        content : 대댓글 내용
         */
        @Schema(description = "질문 게시글 대댓글 내용")
        private String content;

        public static QuestionComment toEntity(Request request, Question question,
            Member register, QuestionComment parent) {
            return QuestionComment.generate(request.getContent(), question, register, parent);
        }
    }

    @Getter
    public static class Response {
        /*
        id : 업무 관리 게시글 댓글 아이디
        content : 댓글 내용
        regAt : 작성 일시
        editAt : 수정 일시
        parentId : 부모 댓글 아이디
         */

        @Schema(description = "질문 게시글 대댓글 아이디")
        private Long id;
        @Schema(description = "질문 게시글 대댓글 내용")
        private String content;
        @Schema(description = "질문 게시글 대댓글 작성 일시")
        private String regAt;
        @Schema(description = "질문 게시글 대댓글 수정 일시")
        private String editAt;
        @Schema(description = "질문 게시글 대댓글 부모 댓글 아이디")
        private Long parentId;


        /**
         * Entity -> Response (Dto)
         *
         * @param questionComment   질문 게시글 댓글 Entity
         * @return  Response (Dto)
         */
        public static Response toDto(QuestionComment questionComment) {
            Response dto = new Response();
            dto.id = questionComment.getId();
            dto.content = questionComment.getContent();
            dto.regAt = DateTimeUtils.format(questionComment.getRegAt());
            dto.editAt = DateTimeUtils.format(questionComment.getEditAt());
            dto.parentId = questionComment.getParent().getId();
            return dto;
        }
    }
}
