package com.checkping.dto.notice.response;

import com.checkping.common.dto.PageMetaResponse;
import com.checkping.domain.notice.Notice;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Getter
public class NoticeListResponse {

    private final List<? extends NoticeGetListWithoutIsdeletedResponse> notices;
    private final Map<String, Object> meta;

    public NoticeListResponse(List<? extends NoticeGetListWithoutIsdeletedResponse> notices, Map<String, Object> meta){
        this.notices = notices;
        this.meta = meta;
    }

    public static NoticeListResponse fromEntityPage(Page<Notice> page, boolean isAdmin){
        List<? extends NoticeGetListWithoutIsdeletedResponse> noticeListResponse = page.getContent()
                .stream()
                .map(NoticeGetListResponse::toDto)
                .toList();

        PageMetaResponse meta = PageMetaResponse.fromPage(page);
        Map<String, Object> result = meta.toMap();

        return new NoticeListResponse(noticeListResponse, result);
    }
}
