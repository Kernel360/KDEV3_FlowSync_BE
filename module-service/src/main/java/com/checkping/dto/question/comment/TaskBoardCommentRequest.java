package com.checkping.dto.question.comment;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskBoardCommentRequest {

    @Getter
    @ToString
    public static class RegisterDto {
        /*
        content : 댓글 내용
        taskBoardId : question 의 id(pk)
         */
        @Schema(description = "게시글 댓글 내용")
        private String content;

        /**
         * RegisterDto -> Entity
         *
         * @param registerDto 등록 정보
         * @param question question (조회한 Entity)
         * @return QuestionComment 엔티티
         */
        public static QuestionComment toEntity (RegisterDto registerDto, Question question) {
            return QuestionComment.builder()
                .content(registerDto.getContent())
                .question(question)
                .build();
        }
    }

    @Getter
    @ToString
    public static class UpdateDto {
        /*
        content : 댓글 내용
         */
        @Schema(description = "게시글 댓글 내용")
        private String content;
    }
}
