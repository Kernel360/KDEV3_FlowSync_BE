package com.checkping.dto.notice.response;

import com.checkping.common.dto.PageMetaResponse;
import com.checkping.domain.notice.Notice;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Getter
public class NoticeListResponse {

    private final List<NoticeGetListResponse> notices;
    private final Map<String, Object> meta;

    public NoticeListResponse(List<NoticeGetListResponse> notices, Map<String, Object> meta){
        this.notices = notices;
        this.meta = meta;
    }

    public static NoticeListResponse fromEntityPage(Page<Notice> page){
        List<NoticeGetListResponse> noticeListResponse = page.getContent()
                .stream()
                .map(NoticeGetListResponse::toDto)
                .toList();

        PageMetaResponse meta = PageMetaResponse.fromPage(page);
        Map<String, Object> result = meta.toMap();

        return new NoticeListResponse(noticeListResponse, result);
    }
}
