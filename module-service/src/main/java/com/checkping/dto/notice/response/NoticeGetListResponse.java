package com.checkping.dto.notice.response;

import com.checkping.domain.notice.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class NoticeGetListResponse extends NoticeGetListWithoutIsdeletedResponse {

    @Schema(description = "공지사항 아이디", example = "1")
    private Long id;

    @Schema(description = "공지사항 글 제목", example = "드릴말씀")
    private String title;

    @Schema(description = "공지사항 글 카테고리", example = "MAINTENANCE")
    private Notice.Category category;

    @Schema(description = "공지사항 글 중요도", example = "EMERGENCY")
    private Notice.Priority priority;

    @Schema(description = "삭제 여부", example = "DEVELOPER")
    private Boolean isDeleted;

    @Schema(description = "생성 날짜", example = "2025-01-27T13:43:33.4716151")
    private LocalDateTime regAt;

    @Schema(description = "수정 날짜", example = "2025-01-28T13:43:33.4716151")
    private LocalDateTime updatedAt;

    public static NoticeGetListResponse toDto(Notice notice){
        return NoticeGetListResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .category(notice.getCategory())
                .priority(notice.getPriority())
                .isDeleted(notice.getIsDeleted())
                .regAt(notice.getUpdatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
}
