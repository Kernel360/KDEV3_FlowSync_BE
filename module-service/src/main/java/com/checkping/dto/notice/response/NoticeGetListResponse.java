package com.checkping.dto.notice.response;

import com.checkping.domain.notice.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeGetListResponse {

    private Long id;

    private Long adminId;

    private String title;

    private Notice.Category category;

    private Notice.Priority priority;

    private Boolean isDeleted;

    private LocalDateTime regAt;

    private LocalDateTime updatedAt;

    public static NoticeGetListResponse toDto(Notice notice){
        return NoticeGetListResponse.builder()
                .id(notice.getId())
                .adminId(notice.getAdminId())
                .title(notice.getTitle())
                .category(notice.getCategory())
                .priority(notice.getPriority())
                .isDeleted(notice.getIsDeleted())
                .regAt(notice.getUpdatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
}
