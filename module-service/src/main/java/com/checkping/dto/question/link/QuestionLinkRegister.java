package com.checkping.dto.question.link;

import com.checkping.domain.question.Question;
import com.checkping.domain.question.QuestionLink;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionLinkRegister {

    @Getter
    @ToString
    public static class Request {
        /*
        name : 게시글 링크 이름
        url : 게시글 링크 url
         */

        @Schema(description = "게시글 링크 이름")
        private String name;
        @Schema(description = "게시글 링크 url")
        private String url;

    }

    /**
     * QuestionLink 등록 요청 Dto -> QuestionLink Entity
     *
     * @param question Question Entity
     * @param request  QuestionLinkRegister.Request Dto
     * @return QuestionLink Entity
     */
    public static QuestionLink toEntity(Question question, QuestionLinkRegister.Request request) {
        return QuestionLink.builder()
            .name(request.getName())
            .url(request.getUrl())
            .question(question)
            .build();
    }

    /**
     * QuestionLink 등록 요청 Dto 리스트 -> QuestionLink Entity 리스트
     *
     * @param question Question Entity
     * @param requests QuestionLinkRegister.Request List
     * @return List<QuestionLink> Entity List
     */
    public static List<QuestionLink> toEntity(Question question,
        List<QuestionLinkRegister.Request> requests) {
        return requests.stream()
            .map(request -> toEntity(question, request))
            .toList();
    }
}
