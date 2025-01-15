package com.checkping.dto;


import com.checkping.domain.board.TaskBoard;
import com.checkping.dto.TaskBoardLinkRequest.RegisterDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskBoardRegister {

    @Getter
    @ToString
    public static class Request {
        /*
        title : 게시글 제목
        content : 게시글 본문 내용
        boardCategory : 게시글 카테고리 (enum, String
        boardStatus : 게시글 상태 (enum, String)
        taskBoardLinkList : 첨부 링크 리스트 (List<TaskBoardLinkRequest.RegisterDto>)
         */
        @Schema(description = "게시글 제목", example = "게시글 제목 입니다.")
        private String title;
        @Schema(description = "게시글 본문", example = "게시글 본문 입니다.")
        private String content;
        @Schema(description = "게시글 유형")
        private TaskBoardCategoryInfo.Request boardCategory;
        @Schema(description = "게시글 상태")
        private TaskBoardStatusInfo.Request boardStatus;
        @Schema(description = "게시글 첨부 링크 목록")
        private List<RegisterDto> taskBoardLinkList;

        /**
         * 업무 관리 게시글 등록 요청 정보로 업무 관리 게시글 엔티티를 만드는 메서드
         *
         * @param request 엄무 관리 게시글 등록 요청 정보
         * @return TaskBoard Entity
         */
        public static TaskBoard toEntity(TaskBoardRegister.Request request) {
            return TaskBoard.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .boardCategory(TaskBoardCategoryInfo.Request.toEntity(request.getBoardCategory()))
                .boardStatus(TaskBoardStatusInfo.Request.toEntity(request.getBoardStatus()))
                .build();
        }
    }

}
