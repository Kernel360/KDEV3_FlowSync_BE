package com.checkping.dto.notice.request;

import com.checkping.domain.notice.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor

public class NoticeCreateRequest {

    @Schema(description = "관리자 아이디", example = "1")
    private Long adminId;

    @Schema(description = "공지사항 글 제목", example = "드릴말씀")
    private String title;

    @Schema(description = "공지사항 글 내용", example = "ABCDEFG")
    private String content;

    @Schema(description = "공지사항 글 카테고리", example = "MAINTENANCE")
    private String category;

    @Schema(description = "공지사항 글 중요도", example = "EMERGENCY")
    private String priority;

    public Notice toEntity() {
        return Notice.builder()
                .title(title)
                .content(content)
                .category(Notice.Category.valueOf(category))
                .priority(Notice.Priority.valueOf(priority))
                .isDeleted(false)
                .build();
    }
}
