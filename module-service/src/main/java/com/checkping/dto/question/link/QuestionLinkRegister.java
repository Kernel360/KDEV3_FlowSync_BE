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

    @Getter
    public static class Response {
        /*
        id : 게시글 링크 아이디
        projectId : 프로젝트 아이디
        name : 게시글 링크 이름
        url : 게시글 링크 url
         */

        private Long id;
        private Long projectId;
        private String name;
        private String url;

        /**
         * QuestionLink Entity -> QuestionLinkRegister.Response Dto
         *
         * @param link QuestionLink Entity
         * @return QuestionLinkRegister.Response Dto
         */
        public static QuestionLinkRegister.Response toDto(QuestionLink link) {
            QuestionLinkRegister.Response dto = new QuestionLinkRegister.Response();
            dto.id = link.getId();
            // TODO : projectId 실제로 찾아서 가져오기 (getProject() 로 변경 예정이기 때문)
            dto.projectId = link.getQuestion().getProjectId();
            dto.name = link.getName();
            dto.url = link.getUrl();
            return dto;
        }

        /**
         * QuestionLink Entity 리스트 -> QuestionLinkRegister.Response Dto 리스트
         *
         * @param links QuestionLink Entity List
         * @return List<QuestionLinkRegister.Response> Dto List
         */
        public static List<QuestionLinkRegister.Response> toDto(List<QuestionLink> links) {
            // Check null or empty
            if (links == null || links.isEmpty()) {
                return List.of();
            }

            return links.stream()
                .map(QuestionLinkRegister.Response::toDto)
                .toList();
        }
    }

}
