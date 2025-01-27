package com.checkping.api.controller.notice;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.notice.request.NoticeCreateRequest;
import com.checkping.dto.notice.request.NoticeUpdateRequest;
import com.checkping.dto.notice.response.NoticeCreateResponse;
import com.checkping.dto.notice.response.NoticeGetListResponse;
import com.checkping.dto.notice.response.NoticeResponse;
import com.checkping.service.notice.NoticeServiceImpl;
import jakarta.websocket.OnError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoticeController implements NoticeApi {

    @Autowired
    private NoticeServiceImpl noticeService;

    @Override
    @PostMapping("/admins/notices")
    public BaseResponse<NoticeCreateResponse> registerNotice(@RequestBody NoticeCreateRequest noticeCreateRequest) {
        NoticeCreateResponse noticeCreateResponse = noticeService.registerNotice(noticeCreateRequest);
        return BaseResponse.success(noticeCreateResponse);
    }

    @Override
    @PatchMapping("/admins/notices/{noticeid}")
    public BaseResponse<NoticeResponse> updateNotice(
            @PathVariable Long noticeid,
            @RequestBody NoticeUpdateRequest noticeUpdateRequest) {
        NoticeResponse noticeResponse = noticeService.updateNotice(noticeid, noticeUpdateRequest);
        return BaseResponse.success(noticeResponse);
    }

    @Override
    @DeleteMapping("/admins/notices/{noticeid}")
    public BaseResponse<NoticeResponse> deleteNotice(
            @PathVariable Long noticeid
    ) {
        NoticeResponse noticeDeleteResponse = noticeService.deleteNotice(noticeid);
        return BaseResponse.success(noticeDeleteResponse);
    }

    @Override
    @GetMapping("/notices")
    public BaseResponse<List<NoticeGetListResponse>> findAllNotices(){
        List<NoticeGetListResponse> result = noticeService.findAllNotices();
        return BaseResponse.success(result);
    }

    @Override
    @GetMapping("/notices/{noticeid}")
    public BaseResponse<NoticeResponse> getNotice(
            @PathVariable Long noticeid
    ){
        NoticeResponse noticeGetResponse = noticeService.getNotice(noticeid);
        return BaseResponse.success(noticeGetResponse);
    }
}
