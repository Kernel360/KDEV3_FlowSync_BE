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
    public static class TaskBoardLinkDto {
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
        public static TaskBoardLinkDto toDto(QuestionLink questionLink) {
            TaskBoardLinkDto taskBoardLinkDto = new TaskBoardLinkDto();
            taskBoardLinkDto.id = questionLink.getId();
            taskBoardLinkDto.name = questionLink.getName();
            taskBoardLinkDto.url = questionLink.getUrl();
            return taskBoardLinkDto;
        }

        /**
         * Entity List -> Dto List
         *
         * @param questionLinkList QuestionLink 엔티티 리스트
         * @return QuestionLinkResponse.TaskBoardLinkDto 리스트
         */
        public static List<TaskBoardLinkDto> toDtoList(List<QuestionLink> questionLinkList) {

            List<TaskBoardLinkDto> taskBoardLinkDtoList = new ArrayList<>();

            // null check
            if (questionLinkList == null) {
                return Collections.emptyList();
            }

            // loop for add
            for (QuestionLink questionLink : questionLinkList) {
                TaskBoardLinkDto taskBoardLinkDto = toDto(questionLink);
                taskBoardLinkDtoList.add(taskBoardLinkDto);
            }

            // return Dto list
            return taskBoardLinkDtoList;
        }
    }
}
