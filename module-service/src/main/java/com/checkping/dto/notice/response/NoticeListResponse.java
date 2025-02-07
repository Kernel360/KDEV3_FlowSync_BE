package com.checkping.dto.notice.response;

import com.checkping.common.dto.PageMetaResponse;
import com.checkping.domain.notice.Notice;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Getter
public class NoticeListResponse {

    private final List<? extends NoticeGetListResponse> notices;
    private final Map<String, Object> meta;

    public NoticeListResponse(List<? extends NoticeGetListResponse> notices, Map<String, Object> meta){
        this.notices = notices;
        this.meta = meta;
    }

    public static NoticeListResponse fromEntityPage(Page<Notice> page, boolean isAdmin){
        List<? extends NoticeGetListResponse> noticeListResponse = page.getContent()
                .stream()
                .map(notice -> isAdmin
                        ? NoticeGetListWithIsdeletedResponse.toDto(notice)  // 관리자
                        : NoticeGetListWithoutIsdeletedResponse.toDto(notice)) // 비관리자
                .toList();

        PageMetaResponse meta = PageMetaResponse.fromPage(page);
        Map<String, Object> result = meta.toMap();

        return new NoticeListResponse(noticeListResponse, result);
    }
}
