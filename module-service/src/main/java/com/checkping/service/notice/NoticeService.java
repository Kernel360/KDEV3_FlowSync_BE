package com.checkping.service.notice;

import com.checkping.dto.notice.request.NoticeCreateRequestDto;
import com.checkping.dto.notice.response.NoticeResponseDto;

public interface NoticeService {

    NoticeResponseDto registerNotice(NoticeCreateRequestDto noticeCreateRequestDto);

}
