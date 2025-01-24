package com.checkping.api.controller.notice;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.notice.request.NoticeCreateRequestDto;
import com.checkping.dto.notice.request.NoticeUpdateRequestDto;
import com.checkping.dto.notice.response.NoticeCreateResponseDto;
import com.checkping.dto.notice.response.NoticeGetListResponseDto;
import com.checkping.dto.notice.response.NoticeUpdateResponseDto;
import com.checkping.service.notice.NoticeServiceImpl;
import jakarta.persistence.Id;
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
    public BaseResponse<NoticeUpdateResponseDto> updateNotice(
            @PathVariable Long noticeid,
            @RequestBody NoticeUpdateRequestDto noticeUpdateRequestDto) {
        NoticeUpdateResponseDto noticeUpdateResponseDto = noticeService.updateNotice(noticeid, noticeUpdateRequestDto);
        return BaseResponse.success(noticeUpdateResponseDto);
    }

    @DeleteMapping("/admins/notices/{noticeid}")
    public BaseResponse<NoticeUpdateResponseDto> deleteNotice(
            @PathVariable Long noticeid
    ) {
        NoticeUpdateResponseDto noticeDeleteResponseDto = noticeService.deleteNotice(noticeid);
        return BaseResponse.success(noticeDeleteResponseDto);
    }

    @GetMapping("/notices")
    public BaseResponse<List<NoticeGetListResponseDto>> findAllNotices(){
        List<NoticeGetListResponseDto> result = noticeService.findAllNotices();
        return BaseResponse.success(result);
    }
}
