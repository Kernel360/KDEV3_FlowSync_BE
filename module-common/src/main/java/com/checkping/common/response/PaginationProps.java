package com.checkping.common.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PaginationProps {

    private Integer currentPage;
    private Integer totalPages;
    private Integer pageSize;
    private Long totalElements;
    private Boolean isFirstPage;
    private Boolean isLastPage;

    public static PaginationProps toDto(Page page) {
        PaginationProps paginationProps = new PaginationProps();
        paginationProps.currentPage = page.getNumber() + 1;
        paginationProps.totalPages = page.getTotalPages();
        paginationProps.pageSize = page.getSize();
        paginationProps.totalElements = page.getTotalElements();
        paginationProps.isFirstPage = page.isFirst();
        paginationProps.isLastPage = page.isLast();
        return paginationProps;
    }
}
