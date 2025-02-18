package com.checkping.dto.notice.response;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.common.utils.FileRequest;
import com.checkping.domain.notice.Notice;
import com.checkping.dto.notice.NoticeContent;
import com.checkping.infra.repository.file.S3FileRepositoryImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

    @Getter
    @Setter
    @Builder
    public class NoticeWithoutIsdeletedResponse implements NoticeResponse {

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

        @Schema(description = "생성 날짜", example = "2025-01-27T13:43:33.4716151")
        private LocalDateTime regAt;

        @Schema(description = "수정 날짜", example = "2025-01-28T13:43:33.4716151")
        private LocalDateTime updatedAt;

        @Schema(description = "공지사항 첨부파일 링크")
        private List<FileRequest> fileInfoList;

        public static NoticeResponse toDto(Notice notice) {
            return NoticeWithoutIsdeletedResponse.builder()
                    .id(notice.getId())
                    .title(notice.getTitle())
                    .content(convertJsonToContentList(notice.getContent()))
                    .category(notice.getCategory())
                    .priority(notice.getPriority())
                    .regAt(notice.getRegAt())
                    .updatedAt(notice.getUpdatedAt())
                    .fileInfoList(convertFileUrlsToFileRequestList(notice.getNoticeFileUrls()))
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

        private static List<FileRequest> convertFileUrlsToFileRequestList(List<String> fileUrls) {
            List<FileRequest> fileRequestList = new ArrayList<>();
            for (String fileUrl : fileUrls) {
                String[] parts = fileUrl.split("\\|");
                fileRequestList.add(
                        new FileRequest(parts[0], parts[0], parts[1], 0L) // 사이즈는 0L로 임시 설정
                );
            }
            return fileRequestList;
        }
    }

