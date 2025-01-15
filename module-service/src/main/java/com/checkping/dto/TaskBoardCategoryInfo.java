package com.checkping.dto;

import com.checkping.domain.board.TaskBoard;
import com.checkping.exception.project.TaskBoardInvalidBoardCategoryException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskBoardCategoryInfo {

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
        public static TaskBoard.BoardCategory toEntity(TaskBoardCategoryInfo.Request request) {
            try {
                return TaskBoard.BoardCategory.valueOf(request.getCode().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new TaskBoardInvalidBoardCategoryException(request.code);
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

        /**
         * 업무 관리 게시글의 게시글 유형 enum 타입을 Dto 로 변경하는 메서드
         *
         * @param boardCategory 업무 관리 게시글 유형
         * @return  업무 관리 게시글 유형 Dto
         */
        public static TaskBoardCategoryInfo.Response toDto(TaskBoard.BoardCategory boardCategory) {
            TaskBoardCategoryInfo.Response dto = new TaskBoardCategoryInfo.Response();
            dto.code = boardCategory.name();
            dto.name = boardCategory.getDescription();
            return dto;
        }

    }


}
