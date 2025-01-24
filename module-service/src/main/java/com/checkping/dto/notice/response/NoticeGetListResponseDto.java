package com.checkping.dto.notice.response;

import com.checkping.domain.notice.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeGetListResponseDto {

    private Long id;

    private Long adminId;

    private String title;

    private Notice.Category category;

    private Notice.Priority priority;

    private LocalDateTime regAt;

    private LocalDateTime updatedAt;

    public static NoticeGetListResponseDto toDto(Notice notice){
        return NoticeGetListResponseDto.builder()
                .id(notice.getId())
                .adminId(notice.getAdminId())
                .title(notice.getTitle())
                .category(notice.getCategory())
                .priority(notice.getPriority())
                .regAt(notice.getUpdatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
}
