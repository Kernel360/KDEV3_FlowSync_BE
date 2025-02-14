package com.checkping.dto.notice.request;

import com.checkping.common.exception.BaseException;
import com.checkping.common.utils.FileRequest;
import com.checkping.domain.notice.Notice;
import com.checkping.dto.notice.NoticeContent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NoticeUpdateRequest {

    @Schema(description = "공지사항 글 제목", example = "드릴말씀")
    private String title;

    @Schema(description = "공지사항 글 내용", example = "ABCDEFG")
    private List<NoticeContent> content;

    @Schema(description = "공지사항 글 카테고리", example = "MAINTENANCE")
    private String category;

    @Schema(description = "공지사항 글 중요도", example = "EMERGENCY")
    private String priority;

    @Schema(description = "공지사항 첨부파일 링크")
    private List<FileRequest> fileInfoList;

    public String convertContentToJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            throw new BaseException();
        }
    }

}
