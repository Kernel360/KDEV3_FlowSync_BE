package com.checkping.dto;

import com.checkping.domain.board.TaskBoard;
import com.checkping.exception.project.TaskBoardInvalidBoardCategoryException;
import com.checkping.exception.project.TaskBoardInvalidBoardStatusException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskBoardRequest {

    @Getter
    @ToString
    public static class RegisterDto {
        /*
        title : 게시글 제목
        content : 게시글 본문 내용
        boardCategory : 게시글 카테고리 (enum, String)
        boardStatus : 게시글 상태 (enum, String)
         */
        private String title;
        private String content;
        private String boardCategory;
        private String boardStatus;

        public static TaskBoard toEntity(RegisterDto registerDto) {
            return TaskBoard.builder()
                .title(registerDto.getTitle())
                .content(registerDto.getContent())
                .boardCategory(TaskBoardRequest.convertBoardCategory(registerDto.getBoardCategory()))
                .boardStatus(TaskBoardRequest.convertBoardStatus(registerDto.getBoardStatus()))
                .build();
        }
    }

    /**
     * Enum : BoardCategory 변환 함수
     *
     * @param value BoardCategory 로 변환할 문자열
     * @return TaskBoard.BoardCategory
     */
    public static TaskBoard.BoardCategory convertBoardCategory(String value) {
        try {
            return TaskBoard.BoardCategory.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TaskBoardInvalidBoardCategoryException(value);
        }
    }

    /**
     * Enum : BoardStatus 변환 함수
     *
     * @param value BoardStatus 로 변환할 문자열
     * @return TaskBoard.BoardStatus
     */
    public static TaskBoard.BoardStatus convertBoardStatus(String value) {
        try {
            return TaskBoard.BoardStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new TaskBoardInvalidBoardStatusException(value);
        }
    }
}
