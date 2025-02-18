package com.checkping.dto.notice.response;

import com.checkping.domain.notice.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@Getter
@Builder
public class NoticeGetListWithoutIsdeletedResponse implements NoticeGetListResponse {

    @Schema(description = "공지사항 아이디", example = "1")
    private Long id;

    @Schema(description = "공지사항 글 제목", example = "드릴말씀")
    private String title;

    @Schema(description = "공지사항 글 카테고리", example = "MAINTENANCE")
    private Notice.Category category;

    @Schema(description = "공지사항 글 중요도", example = "EMERGENCY")
    private Notice.Priority priority;

    @Schema(description = "생성 날짜", example = "2025-01-27T13:43:33.4716151")
    private LocalDateTime regAt;

    @Schema(description = "수정 날짜", example = "2025-01-28T13:43:33.4716151")
    private LocalDateTime updatedAt;

    public static NoticeGetListResponse toDto(Notice notice){
        return NoticeGetListWithoutIsdeletedResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .category(notice.getCategory())
                .priority(notice.getPriority())
                .regAt(notice.getRegAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
}
