package com.checkping.dto.notice.response;

import com.checkping.domain.notice.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
public class NoticeResponse {

    private Long id;

    private Long adminId;

    private String title;

    private String content;

    private Notice.Category category;

    private Notice.Priority priority;

    private Boolean isDeleted;

    private LocalDateTime regAt;

    private LocalDateTime updatedAt;

    public static NoticeResponse toDto(Notice notice){
        return NoticeResponse.builder()
                .id(notice.getId())
                .adminId(notice.getAdminId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .category(notice.getCategory())
                .priority(notice.getPriority())
                .isDeleted(notice.getIsDeleted())
                .regAt(notice.getUpdatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
}
