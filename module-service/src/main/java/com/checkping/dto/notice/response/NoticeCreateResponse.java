package com.checkping.dto.notice.response;


import com.checkping.domain.notice.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeCreateResponse {

    @Schema(description = "공지사항 아이디", example = "1")
    private Long id;

    @Schema(description = "관리자 아이디", example = "1")
    private Long adminId;

    @Schema(description = "공지사항 글 제목", example = "드릴말씀")
    private String title;

    @Schema(description = "공지사항 글 내용", example = "ABCDEFG")
    private String content;

    @Schema(description = "공지사항 글 카테고리", example = "MAINTENANCE")
    private Notice.Category category;

    @Schema(description = "공지사항 글 중요도", example = "EMERGENCY")
    private Notice.Priority priority;

    @Schema(description = "삭제 여부", example = "False")
    private Boolean isDeleted;

    @Schema(description = "생성 날짜", example = "2025-01-27T13:43:33.4716151")
    private LocalDateTime regAt;

    public static NoticeCreateResponse toDto(Notice notice){
        return NoticeCreateResponse.builder()
                .id(notice.getId())
                .adminId(notice.getAdminId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .category(notice.getCategory())
                .priority(notice.getPriority())
                .isDeleted(notice.getIsDeleted())
                .regAt(notice.getRegAt())
                .build();
    }
}
