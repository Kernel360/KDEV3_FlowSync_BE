package com.checkping.service.notice;

import com.checkping.dto.notice.request.NoticeCreateRequest;
import com.checkping.dto.notice.request.NoticeSearchRequest;
import com.checkping.dto.notice.request.NoticeUpdateRequest;
import com.checkping.dto.notice.response.*;

public interface NoticeService {

    NoticeCreateResponse registerNotice(NoticeCreateRequest noticeCreateRequest);

    NoticeResponse updateNotice(Long noticeid, NoticeUpdateRequest noticeUpdateRequest);

    NoticeResponse deleteNotice(Long noticeid);

    NoticeResponse getNotice(Long noticeid);

    NoticeListResponse getNotices(NoticeSearchRequest noticeSearchRequest);

}


