package com.checkping.dto.notice.request;

import com.checkping.domain.notice.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NoticeSearchRequest {

    @Schema(description = "공지사항 목록 검색어", example = "ABC")
    private String keyword;

    @Schema(description = "공지사항 목록 카테고리", example = "MAINTENANCE")
    private String category;

    @Schema(description = "공지사항 목록 페이지", example = "1")
    private int page = 1;
}
