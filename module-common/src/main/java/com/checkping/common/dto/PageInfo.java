package com.checkping.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.Map;

public class PageInfo {
    /*
    currentpage : 페이지 번호
    pageSize : 한 페이지에 나오는 게시글 수

     */
    @Getter
    @Setter
    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {

        @Builder.Default
        @Schema(description = "페이지 번호", example = "1")
        private int currentPage = 1;
        @Builder.Default
        @Schema(description = "게시글 수", example = "10")
        private int pageSize = 10;
        @Schema(description = "검색어")
        private String keyword;
    }

    /*
    dtoList : 전체 조회 목록
    meta : 페이지 정보
     */
    @Getter
    @Setter
    public static class Response<E> {

        private List<E> dtoList;
        private Map<String, Object> meta;

        @Builder
        public Response(List<E> dtoList, Map<String, Object> meta) {
            this.dtoList = dtoList;
            this.meta = meta;
        }

    }

}