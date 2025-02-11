package com.checkping.dto.notice.response;


import com.checkping.common.exception.BaseException;
import com.checkping.common.utils.DateTimeUtils;
import com.checkping.domain.notice.Notice;
import com.checkping.dto.notice.NoticeContent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class NoticeCreateResponse {

    @Schema(description = "공지사항 아이디", example = "1")
    private Long id;

    @Schema(description = "공지사항 글 제목", example = "드릴말씀")
    private String title;

    @Schema(description = "공지사항 글 내용", example = "ABCDEFG")
    private List<NoticeContent> content;

    @Schema(description = "공지사항 글 카테고리", example = "MAINTENANCE")
    private Notice.Category category;

    @Schema(description = "공지사항 글 중요도", example = "EMERGENCY")
    private Notice.Priority priority;

    @Schema(description = "삭제 여부", example = "False")
    private String isDeleted;

    @Schema(description = "생성 날짜", example = "2025-01-27T13:43:33.4716151")
    private LocalDateTime regAt;

    public static NoticeCreateResponse toDto(Notice notice){
        return NoticeCreateResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(convertJsonToContentList(notice.getContent()))
                .category(notice.getCategory())
                .priority(notice.getPriority())
                .isDeleted(notice.getIsDeleted())
                .regAt(notice.getRegAt())
                .build();
    }

    private static List<NoticeContent> convertJsonToContentList(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, new TypeReference<List<NoticeContent>>() {});
        } catch (Exception e) {
            throw new BaseException();
        }
    }
}
