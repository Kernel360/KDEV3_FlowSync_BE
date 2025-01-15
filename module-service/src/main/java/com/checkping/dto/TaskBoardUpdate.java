package com.checkping.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskBoardUpdate {
    @Getter
    @ToString
    public static class Request {
        /*
        title : 게시글 제목
        content : 게시글 본문
        boardCategory : 게시글 유형
        boardStatus : 게시글 상태
         */
        @Schema(description = "게시글 제목", example = "게시글 제목 입니다.")
        private String title;
        @Schema(description = "게시글 본문", example = "게시글 본문 입니다.")
        private String content;
        @Schema(description = "게시글 유형")
        private TaskBoardCategoryInfo.Request boardCategory;
        @Schema(description = "게시글 상태")
        private TaskBoardStatusInfo.Request boardStatus;
    }
}
