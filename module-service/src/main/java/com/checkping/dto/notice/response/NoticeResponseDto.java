package com.checkping.dto.notice.response;

import com.checkping.domain.notice.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
public class NoticeResponseDto {

    private Long id;

    private Long adminId;

    private String title;

    private String content;

    private Notice.Category category;

    private Notice.Priority priority;

    private LocalDateTime regAt;

    private LocalDateTime updatedAt;

    public static NoticeResponseDto toDto(Notice notice){
        return NoticeResponseDto.builder()
                .id(notice.getId())
                .adminId(notice.getAdminId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .category(notice.getCategory())
                .priority(notice.getPriority())
                .regAt(notice.getUpdatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
}
