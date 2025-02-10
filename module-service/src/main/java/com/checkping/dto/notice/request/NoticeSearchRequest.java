package com.checkping.dto.notice.request;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.domain.notice.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NoticeSearchRequest {

    @Schema(description = "공지사항 목록 검색어", example = "ABC")
    private String keyword;

    @Schema(description = "공지사항 목록 카테고리", example = "MAINTENANCE")
    private String category;

    @Schema(description = "공지사항 목록 페이지", example = "1")
    private int page;

    @Schema(description = "한 페이지에 보이는 공지사항 갯수", example = "10")
    private int pageSize;

    @Schema(description = "삭제 여부", example = "true")
    private String isDeleted;

    public Boolean getIsDeletedAsBoolean() {

        if (isDeleted == null || isDeleted.isEmpty()) {
            return null;
        }

        if ("Y".equalsIgnoreCase(isDeleted)) {
            return true;
        } else if ("N".equalsIgnoreCase(isDeleted)) {
            return false;
        }

        throw new BaseException(ErrorCode.BAD_REQUEST);
    }

}
