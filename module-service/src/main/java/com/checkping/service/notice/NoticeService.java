package com.checkping.service.notice;

import com.checkping.dto.notice.request.NoticeCreateRequestDto;
import com.checkping.dto.notice.request.NoticeUpdateRequestDto;
import com.checkping.dto.notice.response.NoticeCreateResponseDto;
import com.checkping.dto.notice.response.NoticeGetListResponseDto;
import com.checkping.dto.notice.response.NoticeResponseDto;

import java.util.List;

public interface NoticeService {

    NoticeCreateResponseDto registerNotice(NoticeCreateRequestDto noticeCreateRequestDto);

    NoticeResponseDto updateNotice(Long noticeid, NoticeUpdateRequestDto noticeUpdateRequestDto);

    NoticeResponseDto deleteNotice(Long noticeid);

    List<NoticeGetListResponseDto> findAllNotices();

    NoticeResponseDto getNotice(Long noticeid);

}
