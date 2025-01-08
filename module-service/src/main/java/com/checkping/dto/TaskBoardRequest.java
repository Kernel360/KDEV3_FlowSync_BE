package com.checkping.dto;

import com.checkping.domain.board.TaskBoard;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class TaskBoardRequest {

    private TaskBoardRequest() {}

    @Getter
    @Setter
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
        private TaskBoard.BoardCategory boardCategory;
        private TaskBoard.BoardStatus boardStatus;

        public static TaskBoard toEntity(RegisterDto registerDto) {
            return TaskBoard.builder()
                .title(registerDto.getTitle())
                .content(registerDto.getContent())
                .boardCategory(registerDto.boardCategory)
                .boardStatus(registerDto.boardStatus)
                .build();
        }
    }
}
