package com.checkping.api.controller.notice;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.notice.request.NoticeCreateRequestDto;
import com.checkping.dto.notice.request.NoticeUpdateRequestDto;
import com.checkping.dto.notice.response.NoticeCreateResponseDto;
import com.checkping.dto.notice.response.NoticeGetListResponseDto;
import com.checkping.dto.notice.response.NoticeResponseDto;
import com.checkping.service.notice.NoticeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    @Autowired
    private NoticeServiceImpl noticeService;

    @PostMapping("/admins/notices")
    public BaseResponse<NoticeCreateResponseDto> registerNotice(@RequestBody NoticeCreateRequestDto noticeCreateRequestDto) {
        NoticeCreateResponseDto noticeCreateResponseDto = noticeService.registerNotice(noticeCreateRequestDto);
        return BaseResponse.success(noticeCreateResponseDto);
    }

    @PatchMapping("/admins/notices/{noticeid}")
    public BaseResponse<NoticeResponseDto> updateNotice(
            @PathVariable Long noticeid,
            @RequestBody NoticeUpdateRequestDto noticeUpdateRequestDto) {
        NoticeResponseDto noticeResponseDto = noticeService.updateNotice(noticeid, noticeUpdateRequestDto);
        return BaseResponse.success(noticeResponseDto);
    }

    @DeleteMapping("/admins/notices/{noticeid}")
    public BaseResponse<NoticeResponseDto> deleteNotice(
            @PathVariable Long noticeid
    ) {
        NoticeResponseDto noticeDeleteResponse = noticeService.deleteNotice(noticeid);
        return BaseResponse.success(noticeDeleteResponse);
    }

    @GetMapping("/notices")
    public BaseResponse<List<NoticeGetListResponseDto>> findAllNotices(){
        List<NoticeGetListResponseDto> result = noticeService.findAllNotices();
        return BaseResponse.success(result);
    }

    @GetMapping("/notices/{noticeid}")
    public BaseResponse<NoticeResponseDto> getNotice(
            @PathVariable Long noticeid
    ){
        NoticeResponseDto noticeGetResponse = noticeService.getNotice(noticeid);
        return BaseResponse.success(noticeGetResponse);
    }
}
