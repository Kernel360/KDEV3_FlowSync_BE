package com.checkping.service.notice;

import com.checkping.domain.notice.Notice;
import com.checkping.dto.notice.request.NoticeCreateRequest;
import com.checkping.dto.notice.request.NoticeSearchRequest;
import com.checkping.dto.notice.request.NoticeUpdateRequest;
import com.checkping.dto.notice.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface NoticeService {

    NoticeCreateResponse registerNotice(NoticeCreateRequest noticeCreateRequest);

    NoticeResponse updateNotice(Long noticeid, NoticeUpdateRequest noticeUpdateRequest);

    NoticeResponse deleteNotice(Long noticeid);

    Map<String, Object> getNotice(Long noticeid);

    Map<String, Object> getNotices(NoticeSearchRequest noticeSearchRequest);

}


