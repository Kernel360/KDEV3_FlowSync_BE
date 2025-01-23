package com.checkping.service.notice;

import com.checkping.dto.notice.request.NoticeCreateRequestDto;
import com.checkping.dto.notice.response.NoticeCreateResponseDto;

public interface NoticeService {

    NoticeCreateResponseDto registerNotice(NoticeCreateRequestDto noticeCreateRequestDto);

}
