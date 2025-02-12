package com.checkping.dto.question.comment;

import com.checkping.domain.question.QuestionComment;
import com.checkping.domain.question.QuestionComment.DeleteStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionCommentResponse {

    @Getter
    @ToString
    public static class QuestionCommentDto {

        @Schema(description = "게시글 댓글 ID")
        private Long id;
        @Schema(description = "게시글 댓글 내용")
        private String content;
        @Schema(description = "게시글 댓글 등록 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime regAt;
        @Schema(description = "게시글 댓글 수정 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime editAt;
        private DeleteStatus deletedYn;

        /**
         * Entity -> Dto
         *
         * @param questionComment QuestionComment Entity
         * @return QuestionCommentDto
         */
        public static QuestionCommentDto toDto(QuestionComment questionComment) {
            QuestionCommentDto dto = new QuestionCommentDto();
            dto.id = questionComment.getId();
            dto.content = questionComment.getContent();
            dto.regAt = questionComment.getRegAt();
            dto.editAt = questionComment.getEditAt();
            dto.deletedYn = questionComment.getDeletedYn();
            return dto;
        }

        /**
         * QuestionComment 리스트를 QuestionCommentDto 리스트로 변환
         *
         * @param questionCommentList QuestionComment 의 리스트
         * @return QuestionCommentDto 리스트
         */
        public static List<QuestionCommentDto> toDtoList(
            List<QuestionComment> questionCommentList) {

            // null check
            if (questionCommentList == null) {
                return Collections.emptyList();
            }

            return questionCommentList.stream().map(QuestionCommentDto::toDto).toList();
        }

    }
}
