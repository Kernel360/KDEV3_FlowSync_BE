package com.checkping.dto.question.link;

import com.checkping.domain.question.QuestionLink;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuestionLinkResponse {

    @Getter
    @ToString
    public static class QuestionLinkDto {
        @Schema(description = "게시글 링크 ID")
        private Long id;
        @Schema(description = "게시글 링크 이름")
        private String name;
        @Schema(description = "게시글 링크 URL")
        private String url;

        /**
         * Entity -> Dto
         *
         * @param questionLink QuestionLink Entity
         * @return QuestionLinkResponse.toDto
         */
        public static QuestionLinkDto toDto(QuestionLink questionLink) {
            QuestionLinkDto questionLinkDto = new QuestionLinkDto();
            questionLinkDto.id = questionLink.getId();
            questionLinkDto.name = questionLink.getName();
            questionLinkDto.url = questionLink.getUrl();
            return questionLinkDto;
        }

        /**
         * Entity List -> Dto List
         *
         * @param questionLinkList QuestionLink 엔티티 리스트
         * @return QuestionLinkResponse.QuestionLinkDto 리스트
         */
        public static List<QuestionLinkDto> toDtoList(List<QuestionLink> questionLinkList) {

            List<QuestionLinkDto> questionLinkDtoList = new ArrayList<>();

            // null check
            if (questionLinkList == null) {
                return Collections.emptyList();
            }

            // loop for add
            for (QuestionLink questionLink : questionLinkList) {
                QuestionLinkDto questionLinkDto = toDto(questionLink);
                questionLinkDtoList.add(questionLinkDto);
            }

            // return Dto list
            return questionLinkDtoList;
        }
    }
}
