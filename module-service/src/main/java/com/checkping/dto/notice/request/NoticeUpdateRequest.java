package com.checkping.dto.notice.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NoticeUpdateRequest {

    @Schema(description = "공지사항 글 제목", example = "드릴말씀")
    private String title;

    @Schema(description = "공지사항 글 내용", example = "ABCDEFG")
    private String content;

    @Schema(description = "공지사항 글 카테고리", example = "MAINTENANCE")
    private String category;

    @Schema(description = "공지사항 글 중요도", example = "EMERGENCY")
    private String priority;

}
