package com.checkping.service.notice;

import com.checkping.dto.notice.request.NoticeCreateRequest;
import com.checkping.dto.notice.request.NoticeSearchRequest;
import com.checkping.dto.notice.request.NoticeUpdateRequest;
import com.checkping.dto.notice.response.*;
import org.springframework.web.multipart.MultipartFile;

public interface NoticeService {

    NoticeCreateResponse registerNotice(NoticeCreateRequest noticeCreateRequest, MultipartFile file);

    NoticeResponse updateNotice(Long noticeid, NoticeUpdateRequest noticeUpdateRequest, MultipartFile file);

    NoticeResponse deleteNotice(Long noticeid);

    NoticeResponse getNotice(Long noticeid);

    NoticeListResponse getNotices(NoticeSearchRequest noticeSearchRequest);

}


