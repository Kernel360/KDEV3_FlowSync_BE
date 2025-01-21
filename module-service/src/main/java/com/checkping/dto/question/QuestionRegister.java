package com.checkping.dto.question;

import com.checkping.common.utils.FileRequest;
import com.checkping.domain.question.Question;
import com.checkping.dto.question.link.QuestionLinkRequest.RegisterDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
         */
        @Schema(description = "게시글 제목", example = "게시글 제목 입니다.")
        private String title;
        @Schema(description = "게시글 본문", example = "게시글 본문 입니다.")
        private String content;
        @Schema(description = "게시글 첨부 링크 목록")
        private List<RegisterDto> linkList;
        @Schema(description = "게시글 첨부 파일 목록")
        private List<FileRequest> fileInfoList;

        /**
         * 업무 관리 게시글 등록 요청 정보로 업무 관리 게시글 엔티티를 만드는 메서드
         *
         * @param registerDto 엄무 관리 게시글 등록 요청 정보
         * @return Question Entity
         */
        public static Question toEntity(QuestionRegister.Request registerDto) {
            return Question.builder().title(registerDto.getTitle())
                .content(registerDto.getContent()).build();
        }
    }


}
