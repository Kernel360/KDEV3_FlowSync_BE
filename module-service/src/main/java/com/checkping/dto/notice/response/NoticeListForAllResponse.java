package com.checkping.dto.notice.response;

import com.checkping.common.dto.PageMetaResponse;
import com.checkping.domain.notice.Notice;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Getter
public class NoticeListForAllResponse {

    private final List<NoticeGetListForAllResponse> notices;
    private final Map<String, Object> meta;

    public NoticeListForAllResponse(List<NoticeGetListForAllResponse> notices, Map<String, Object> meta){
        this.notices = notices;
        this.meta = meta;
    }

    public static NoticeListForAllResponse fromEntityPage(Page<Notice> page){
        List<NoticeGetListForAllResponse> noticeListForAllResponse = page.getContent()
                .stream()
                .map(NoticeGetListForAllResponse::toDto)
                .toList();

        PageMetaResponse meta = PageMetaResponse.fromPage(page);
        Map<String, Object> result = meta.toMap();

        return new NoticeListForAllResponse(noticeListForAllResponse, result);
    }
}
