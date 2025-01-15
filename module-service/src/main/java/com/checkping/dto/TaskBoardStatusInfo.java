package com.checkping.dto;

import com.checkping.domain.board.TaskBoard;
import com.checkping.exception.project.TaskBoardInvalidBoardCategoryException;
import com.checkping.exception.project.TaskBoardInvalidBoardStatusException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskBoardStatusInfo {

    @Getter
    @ToString
    public static class Request {
        /*
        code : 업무 관리 게시글 코드
        name : 업무 관리 게시글 이름(한글)
         */
        private String code;
        private String name;

        /**
         * Request Dto -> Entity Enum 변환 함순
         *
         * @param request 요청으로 온 BoardCategory
         * @return TaskBoard.BoardCategory(Entity Enum)
         */
        public static TaskBoard.BoardStatus toEntity(TaskBoardStatusInfo.Request request) {
            try {
                return TaskBoard.BoardStatus.valueOf(request.getCode().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new TaskBoardInvalidBoardStatusException(request.code);
            }
        }
    }

    @Getter
    @ToString
    public static class Response {
        /*
        code : 업무 관리 게시글 코드
        name : 업무 관리 게시글 이름(한글)
         */
        private String code;
        private String name;
    }
}
