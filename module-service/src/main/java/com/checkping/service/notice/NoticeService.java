package com.checkping.service.notice;

import com.checkping.dto.notice.request.NoticeCreateRequestDto;
import com.checkping.dto.notice.request.NoticeUpdateRequestDto;
import com.checkping.dto.notice.response.NoticeCreateResponseDto;
import com.checkping.dto.notice.response.NoticeUpdateResponseDto;

public interface NoticeService {

    NoticeCreateResponseDto registerNotice(NoticeCreateRequestDto noticeCreateRequestDto);

    NoticeUpdateResponseDto updateNotice(Long noticeid, NoticeUpdateRequestDto noticeUpdateRequestDto);

}
