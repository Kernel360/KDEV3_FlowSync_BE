package com.checkping.dto.question;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.Question.Category;
import com.checkping.domain.question.Question.Status;
import com.checkping.dto.question.comment.QuestionCommentGet;
import com.checkping.dto.question.file.QuestionFileRegister;
import com.checkping.dto.question.link.QuestionLinkRegister;
import com.checkping.exception.question.QuestionContentParsingException;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionGet {

    @Getter
    public static class Response {

        /*
        id : 게시글 고유 ID
        number : 게시글 번호
        title : 게시글 제목
        content : 게시글 본문 내용
        regAt : 게시글 작성 일시
        editAt : 게시글 마지막 수정 일시
        category : 게시글 카테고리 (enum, String)
        status : 게시글 상태 (enum, String)
        commentList : 게시글 댓글 리스트
        linkList : 게시글 첨부 링크 리스트
        fileList : 게시글 첨부 파일 리스트
         */
        @Schema(description = "게시글 번호")
        private Long id;
        @Schema(description = "게시글 번호")
        private Integer number;
        @Schema(description = "게시글 제목", example = "게시글 제목 입니다.")
        private String title;
        @Schema(description = "게시글 본문", example = "게시글 본문 입니다.")
        private List<QuestionContent> content;
        @Schema(description = "등록 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime regAt;
        @Schema(description = "수정 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime editAt;
        @Schema(description = "게시글 유형")
        private Category category;
        @Schema(description = "게시글 상태")
        private Status status;
        @Schema(description = "게시글 댓글 목록")
        private List<QuestionCommentGet.Response> commentList;
        @Schema(description = "게시글 첨부 링크 목록")
        private List<QuestionLinkRegister.Response> linkList;
        @Schema(description = "게시글 첨부 파일 목록")
        private List<QuestionFileRegister.Response> fileList;

        public static Response toDto(Question question) {
            Response response = new Response();
            response.id = question.getId();
            response.number = question.getNumber();
            response.title = question.getTitle();
            response.content = QuestionGet.Response.toContentList(question.getContent());
            response.regAt = question.getRegAt();
            response.editAt = question.getEditAt();
            response.category = question.getCategory();
            response.status = question.getStatus();
            response.commentList = QuestionCommentGet.Response.toDto(question.getCommentList());
            response.linkList = QuestionLinkRegister.Response.toDto(question.getQuestionLinkList());
            response.fileList = QuestionFileRegister.Response.toDto(question.getQuestionFileList());
            return response;
        }

        /**
         * String -> JSON LIST
         *
         * @param content content String
         * @return content List
         */
        private static List<QuestionContent> toContentList(String content) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(content, new TypeReference<List<QuestionContent>>() {
                });
            } catch (Exception e) {
                throw new QuestionContentParsingException();
            }
        }
    }

}
