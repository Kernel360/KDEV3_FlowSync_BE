package com.checkping.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        private int currentpage = 1;
        @Builder.Default
        @Schema(description = "게시글 수", example = "10")
        private int pageSize = 10;
        @Schema(description = "검색어")
        private String keyword;
    }

    /*
    dtoList :
    pageNumList :
    pageRequest :
    prev :
    next :
    totalCount :
    prevPage :
    nextPage :
    totalPage :
    current :
     */
    @Getter
    @Setter
    public static class Response<E> {

        private List<E> dtoList;
        private List<Integer> pageNumList;
        private PageInfo.Request pageRequest;
        private boolean prev, next;
        private int totalCount, prevPage, nextPage, totalPage, current;

        @Builder
        public Response(List<E> dtoList, PageInfo.Request pageRequest, long totalCount) {
            this.dtoList = dtoList;
            this.pageRequest = pageRequest;
            this.totalCount = (int) totalCount;
            int end = (int) (Math.ceil(pageRequest.getCurrentpage() / 10.0)) * 10;
            int start = end - 9;
            int last = (int) (Math.ceil((totalCount / (double) pageRequest.getPageSize())));
            end = Math.min(end, last);
            this.prev = start > 1;
            this.next = totalCount > end * pageRequest.getPageSize();
            this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
            if (prev) {
                this.prevPage = start - 1;
            }
            if (next) {
                this.nextPage = end + 1;
            }
            this.totalPage = this.pageNumList.size();
            this.current = pageRequest.getCurrentpage();
        }
    }

}