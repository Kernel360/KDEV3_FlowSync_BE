package com.checkping.dto.notice.response;


import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.common.utils.DateTimeUtils;
import com.checkping.common.utils.FileRequest;
import com.checkping.domain.notice.Notice;
import com.checkping.dto.notice.NoticeContent;
import com.checkping.infra.repository.file.S3FileRepositoryImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private String regAt;

    @Schema(description = "공지사항 첨부파일 링크")
    private List<FileRequest> fileInfoList;

    public static NoticeCreateResponse toDto(Notice notice){
        return NoticeCreateResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(convertJsonToContentList(notice.getContent()))
                .category(notice.getCategory())
                .priority(notice.getPriority())
                .isDeleted(notice.getIsDeleted() != null && notice.getIsDeleted() ? "Y" : "N")
                .regAt(DateTimeUtils.format(notice.getRegAt()))
                .fileInfoList(notice.getNoticeFileUrls().stream()
                        .map(url -> {
                            String[] parts = url.split("\\|");
                            return new FileRequest(parts[0], parts[1], parts[1], 0); // size는 0으로 설정
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    private static List<NoticeContent> convertJsonToContentList(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, new TypeReference<List<NoticeContent>>() {
            });
        } catch (Exception e) {
            throw new BaseException();
        }
    }
}
