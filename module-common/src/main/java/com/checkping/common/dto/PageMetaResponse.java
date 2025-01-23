package com.checkping.common.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public class PageMetaResponse {
    private final long totalElements;
    private final boolean isFirstPage;
    private final boolean isLastPage;
    private final int totalPages;
    private final int pageSize;
    private final int currentPage;

    public PageMetaResponse(long totalElements, boolean isFirstPage, boolean isLastPage, int totalPages, int pageSize, int currentPage) {
        this.totalElements = totalElements;
        this.isFirstPage = isFirstPage;
        this.isLastPage = isLastPage;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }

    public static PageMetaResponse fromPage(org.springframework.data.domain.Page<?> page) {
        return new PageMetaResponse(
                page.getTotalElements(),
                page.isFirst(),
                page.isLast(),
                page.getTotalPages(),
                page.getSize(),
                page.getNumber() + 1
        );
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "totalElements", totalElements,
                "isFirstPage", isFirstPage,
                "isLastPage", isLastPage,
                "totalPages", totalPages,
                "pageSize", pageSize,
                "currentPage", currentPage
        );
    }
}