package com.checkping.service.notice;

import com.checkping.dto.notice.request.NoticeCreateRequest;
import com.checkping.dto.notice.request.NoticeUpdateRequest;
import com.checkping.dto.notice.response.NoticeCreateResponse;
import com.checkping.dto.notice.response.NoticeGetListResponse;
import com.checkping.dto.notice.response.NoticeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeService {

    NoticeCreateResponse registerNotice(NoticeCreateRequest noticeCreateRequest);

    NoticeResponse updateNotice(Long noticeid, NoticeUpdateRequest noticeUpdateRequest);

    NoticeResponse deleteNotice(Long noticeid);

    Page<NoticeGetListResponse> findAllNotices(Pageable pageable);

    NoticeResponse getNotice(Long noticeid);

}
