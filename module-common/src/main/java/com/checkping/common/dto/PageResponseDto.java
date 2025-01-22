package com.checkping.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
public class PageResponseDto<E> {

    private List<E> dtoList;
    private List<Integer> pageNumList;
    private PageRequestDto pageRequestDto;
    private boolean prev, next;
    private int totalCount, prevPage, nextPage, totalPage, current;

    @Builder
    public PageResponseDto(List<E> dtoList, PageRequestDto pageRequestDto, long totalCount) {
        this.dtoList = dtoList;
        this.pageRequestDto = pageRequestDto;
        this.totalCount = (int) totalCount;

        int end = (int) (Math.ceil(pageRequestDto.getPage() / 10.0)) * 10;

        int start = end - 9;

        int last = (int) (Math.ceil((totalCount / (double) pageRequestDto.getSize())));

        end = Math.min(end, last);

        this.prev = start > 1;

        this.next = totalCount > end * pageRequestDto.getSize();

        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        if (prev) {
            this.prevPage = start - 1;
        }

        if (next) {
            this.nextPage = end + 1;
        }

        this.totalPage = this.pageNumList.size();

        this.current = pageRequestDto.getPage();
    }

}