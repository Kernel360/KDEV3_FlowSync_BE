package com.checkping.dto.notice.response;

import com.checkping.domain.notice.Notice;
import com.checkping.dto.notice.request.NoticeUpdateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
public class NoticeUpdateResponseDto {

    private Long adminId;

    private String title;

    private String content;

    private Notice.Category category;

    private Notice.Priority priority;

    private LocalDateTime regAt;

    private LocalDateTime updatedAt;

    public static NoticeUpdateResponseDto toDto(Notice notice){
        return NoticeUpdateResponseDto.builder()
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
