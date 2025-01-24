package com.checkping.service.notice;

import com.checkping.dto.notice.request.NoticeCreateRequestDto;
import com.checkping.dto.notice.request.NoticeUpdateRequestDto;
import com.checkping.dto.notice.response.NoticeCreateResponseDto;
import com.checkping.dto.notice.response.NoticeGetListResponseDto;
import com.checkping.dto.notice.response.NoticeUpdateResponseDto;

import java.util.List;

public interface NoticeService {

    NoticeCreateResponseDto registerNotice(NoticeCreateRequestDto noticeCreateRequestDto);

    NoticeUpdateResponseDto updateNotice(Long noticeid, NoticeUpdateRequestDto noticeUpdateRequestDto);

    NoticeUpdateResponseDto deleteNotice(Long noticeid);

    List<NoticeGetListResponseDto> findAllNotices();

    NoticeUpdateResponseDto getNotice(Long noticeid);

}
