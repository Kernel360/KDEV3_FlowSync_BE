package com.checkping.dto;

import com.checkping.domain.board.TaskBoardLink;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskBoardLinkResponse {

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
         * @param taskBoardLink TaskBoardLink Entity
         * @return TaskBoardLinkResponse.toDto
         */
        public static TaskBoardLinkDto toDto(TaskBoardLink taskBoardLink) {
            TaskBoardLinkDto taskBoardLinkDto = new TaskBoardLinkDto();
            taskBoardLinkDto.id = taskBoardLink.getId();
            taskBoardLinkDto.name = taskBoardLink.getName();
            taskBoardLinkDto.url = taskBoardLink.getUrl();
            return taskBoardLinkDto;
        }

        /**
         * Entity List -> Dto List
         *
         * @param taskBoardLinkList TaskBoardLink 엔티티 리스트
         * @return TaskBoardLinkResponse.TaskBoardLinkDto 리스트
         */
        public static List<TaskBoardLinkDto> toDtoList(List<TaskBoardLink> taskBoardLinkList) {

            List<TaskBoardLinkDto> taskBoardLinkDtoList = new ArrayList<>();

            // null check
            if (taskBoardLinkList == null) {
                return Collections.emptyList();
            }

            // loop for add
            for (TaskBoardLink taskBoardLink : taskBoardLinkList) {
                TaskBoardLinkDto taskBoardLinkDto = toDto(taskBoardLink);
                taskBoardLinkDtoList.add(taskBoardLinkDto);
            }

            // return Dto list
            return taskBoardLinkDtoList;
        }
    }
}
