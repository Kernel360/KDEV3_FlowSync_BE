package com.checkping.dto.question;

import com.checkping.common.utils.FileResponse;
import com.checkping.domain.question.Question;
import com.checkping.domain.question.Question.Category;
import com.checkping.domain.question.Question.Status;
import com.checkping.dto.question.comment.QuestionCommentResponse.QuestionCommentDto;
import com.checkping.dto.question.link.QuestionLinkResponse.QuestionLinkDto;
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
public class QuestionResponse {

    @Getter
    @Setter
    @ToString
    public static class QuestionListDto {

        /*
        id : 게시글 고유 ID
        number : 게시글 번호
        title : 게시글 제목
        content : 게시글 본문 내용
        regAt : 게시글 작성 일시
        editAt : 게시글 마지막 수정 일시
        approverAt : 게시글 승인 일시
        category : 게시글 카테고리 (enum, String)
        status : 게시글 상태 (enum, String)
        deletedYn : 게시글 삭제 여부 ('Y' 또는 'N')
         */
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
        @Schema(description = "승인 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime approverAt;
        @Schema(description = "게시글 유형")
        private Category category;
        @Schema(description = "게시글 상태")
        private Status status;
        private Question.DeleteStatus deletedYn;

        public static QuestionListDto toDto(Question question) {
            QuestionListDto boardDto = new QuestionListDto();
            boardDto.setId(question.getId());
            boardDto.setNumber(question.getNumber());
            boardDto.setTitle(question.getTitle());
            boardDto.setContent(question.getContent());
            boardDto.setRegAt(question.getRegAt());
            boardDto.setEditAt(question.getEditAt());
            boardDto.setCategory(question.getCategory());
            boardDto.setStatus(question.getStatus());
            boardDto.setDeletedYn(question.getDeletedYn());
            return boardDto;
        }
    }

    @Getter
    @Setter
    @ToString
    public static class QuestionItemDto {

        /*
        id : 게시글 고유 ID
        number : 게시글 번호
        title : 게시글 제목
        content : 게시글 본문 내용
        regAt : 게시글 작성 일시
        editAt : 게시글 마지막 수정 일시
        approverAt : 게시글 승인 일시
        category : 게시글 카테고리 (enum, String)
        status : 게시글 상태 (enum, String)
        deletedYn : 게시글 삭제 여부 ('Y' 또는 'N')
        commentList : 게시글 댓글 리스트
         */
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
        @Schema(description = "승인 일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime approverAt;
        @Schema(description = "게시글 유형")
        private Category category;
        @Schema(description = "게시글 상태")
        private Status status;
        private Question.DeleteStatus deletedYn;
        @Schema(description = "게시글 댓글 목록")
        private List<QuestionCommentDto> commentList;
        @Schema(description = "게시글 첨부 링크 목록")
        private List<QuestionLinkDto> linkList;
        @Schema(description = "게시글 첨부 파일 목록")
        private List<FileResponse> fileList;

        public static QuestionItemDto toDto(Question question) {
            QuestionItemDto boardDto = new QuestionItemDto();
            boardDto.setId(question.getId());
            boardDto.setNumber(question.getNumber());
            boardDto.setTitle(question.getTitle());
            boardDto.setContent(question.getContent());
            boardDto.setRegAt(question.getRegAt());
            boardDto.setEditAt(question.getEditAt());
            boardDto.setCategory(question.getCategory());
            boardDto.setStatus(question.getStatus());
            boardDto.setDeletedYn(question.getDeletedYn());

            // Entity -> Dto (QuestionComment)
            List<QuestionCommentDto> comments =
                QuestionCommentDto.toDtoList(question.getCommentList());
            boardDto.setCommentList(comments);

            // Entity -> Dto (QuestionLink)
            List<QuestionLinkDto> links = QuestionLinkDto.toDtoList(
                question.getQuestionLinkList());
            boardDto.setLinkList(links);

            // Entity -> Dto (QuestionFile)
            List<FileResponse> fileList = FileResponse.toDtoList(question.getQuestionFileList());
            boardDto.setFileList(fileList);

            return boardDto;
        }
    }
}
