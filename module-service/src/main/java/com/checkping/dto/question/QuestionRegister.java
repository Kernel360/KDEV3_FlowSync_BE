package com.checkping.dto.question;

import com.checkping.common.utils.FileRequest;
import com.checkping.domain.question.Question;
import com.checkping.domain.question.Question.Category;
import com.checkping.domain.question.Question.Status;
import com.checkping.dto.question.comment.QuestionCommentResponse.QuestionCommentDto;
import com.checkping.dto.question.file.QuestionFileRegister;
import com.checkping.dto.question.link.QuestionLinkRegister;
import com.checkping.exception.question.QuestionCategoryException;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionRegister {

    @Getter
    @ToString
    public static class Request {

        /*
        title : 게시글 제목
        content : 게시글 본문 내용
        linkList : 첨부 링크 리스트 (List<QuestionLinkRequest.RegisterDto>)
        fileInfoList : 첨부 파일 정보 리스트 (List<FileRequest>)
        category : 게시글 카테고리 (enum, String)
        progressStepId : 진행 단계 ID
         */
        @Schema(description = "게시글 제목", example = "게시글 제목 입니다.")
        private String title;
        @Schema(description = "게시글 본문", example = "게시글 본문 입니다.")
        private String content;
        @Schema(description = "게시글 첨부 링크 목록")
        private List<QuestionLinkRegister.Request> linkList;
        @Schema(description = "게시글 첨부 파일 목록")
        private List<FileRequest> fileInfoList;
        @Schema(description = "진행 단계 ID", example = "1")
        private Long progressStepId;

        /**
         * 업무 관리 게시글 등록 요청 정보로 업무 관리 게시글 엔티티를 만드는 메서드
         *
         * @param registerDto 엄무 관리 게시글 등록 요청 정보
         * @return Question Entity
         */
        public static Question toEntity(Long projectId,
            QuestionRegister.Request registerDto) {
            return Question.generate(projectId, registerDto.getProgressStepId(),
                registerDto.getTitle(), registerDto.getContent(),
                Category.QUESTION);
        }
    }

    @Getter
    @Setter(value = AccessLevel.PRIVATE)
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
        private String content;
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
        private List<QuestionCommentDto> commentList;
        @Schema(description = "게시글 첨부 링크 목록")
        private List<QuestionLinkRegister.Response> linkList;
        @Schema(description = "게시글 첨부 파일 목록")
        private List<QuestionFileRegister.Response> fileList;

        public static Response toDto(Question question) {
            Response questionDto = new Response();
            questionDto.setId(question.getId());
            questionDto.setNumber(question.getNumber());
            questionDto.setTitle(question.getTitle());
            questionDto.setContent(question.getContent());
            questionDto.setRegAt(question.getRegAt());
            questionDto.setEditAt(question.getEditAt());
            questionDto.setCategory(question.getCategory());
            questionDto.setStatus(question.getStatus());
            questionDto.setFileList(
                QuestionFileRegister.Response.toDto(question.getQuestionFileList()));
            questionDto.setLinkList(
                QuestionLinkRegister.Response.toDto(question.getQuestionLinkList()));
            return questionDto;
        }
    }

    /**
     * Enum : Category 변환 함수
     *
     * @param value Category 로 변환할 문자열
     * @return Question.Category
     */
    public static Category convertCategory(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            return Category.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new QuestionCategoryException(value);
        }
    }
}
