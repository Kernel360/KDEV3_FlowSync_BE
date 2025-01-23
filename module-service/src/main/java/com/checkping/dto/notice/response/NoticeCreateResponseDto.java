package com.checkping.dto.notice.response;


import com.checkping.domain.notice.Notice;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeCreateResponseDto {
    private Long adminId;

    private String title;

    private String content;

    private Notice.Category category;

    private Notice.Priority priority;

    private LocalDateTime regAt;

    public static NoticeCreateResponseDto toDto(Notice notice){
        return NoticeCreateResponseDto.builder()
                .adminId(notice.getAdminId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .category(notice.getCategory())
                .priority(notice.getPriority())
                .regAt(notice.getRegAt())
                .build();
    }
}
