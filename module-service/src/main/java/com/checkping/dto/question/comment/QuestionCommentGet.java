package com.checkping.dto.question.comment;

import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.question.QuestionComment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionCommentGet {

    @Getter
    public static class Response {

        /*
        id : 질문 게시글 댓글 아이디
        content : 댓글 내용
        regAt : 작성 일시
        editAt : 수정 일시
        parentId : 부모 댓글 아이디
         */
        @Schema(description = "질문 게시글 댓글 아이디")
        private Long id;
        @Schema(description = "질문 게시글 댓글 내용")
        private String content;
        @Schema(description = "질문 게시글 댓글 작성 일시")
        private String regAt;
        @Schema(description = "질문 게시글 댓글 수정 일시")
        private String editAt;
        @Schema(description = "질문 게시글 댓글 부모 댓글 아이디")
        private Long parentId;

        /**
         * 질문 게시글 댓글 엔티티를 응답 정보로 변환하는 메서드
         *
         * @param comment 질문 게시글 댓글 엔티티
         * @return 질문 게시글 댓글 응답 정보
         */
        public static Response toDto(QuestionComment comment) {
            Response response = new Response();
            response.id = comment.getId();
            response.content = comment.getContent();
            response.regAt = DateTimeUtils.format(comment.getRegAt());
            response.editAt = DateTimeUtils.format(comment.getEditAt());
            response.parentId = comment.getParent() == null ? null : comment.getParent().getId();
            return response;
        }

        /**
         * 질문 게시글 댓글 엔티티 리스트를 응답 정보 리스트로 변환하는 메서드
         *
         * @param comments 질문 게시글 댓글 엔티티 리스트
         * @return 질문 게시글 댓글 응답 정보 리스트
         */
        public static List<Response> toDto(List<QuestionComment> comments) {
            return comments.stream()
                .map(Response::toDto)
                .toList();
        }
    }

}
