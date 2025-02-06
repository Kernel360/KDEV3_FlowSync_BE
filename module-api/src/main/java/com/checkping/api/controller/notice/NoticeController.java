package com.checkping.api.controller.notice;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.notice.request.NoticeCreateRequest;
import com.checkping.dto.notice.request.NoticeSearchRequest;
import com.checkping.dto.notice.request.NoticeUpdateRequest;
import com.checkping.dto.notice.response.*;
import com.checkping.service.notice.NoticeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class NoticeController implements NoticeApi {

    private final NoticeServiceImpl noticeService;

    @Override
    @PostMapping("/admins/notices")
    public BaseResponse<NoticeCreateResponse> registerNotice(@RequestBody NoticeCreateRequest noticeCreateRequest) {
        NoticeCreateResponse noticeCreateResponse = noticeService.registerNotice(noticeCreateRequest);
        return BaseResponse.success(noticeCreateResponse);
    }

    @Override
    @PutMapping("/admins/notices/{noticeid}")
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
    @GetMapping("/notices/{noticeid}")
    public BaseResponse<Map<String, Object>> getNotice(
            @PathVariable Long noticeid
    ){
        Map<String, Object> noticeGetResponse = noticeService.getNotice(noticeid);
        return BaseResponse.success(noticeGetResponse);
    }

    @Override
    @GetMapping("/notices")
    public BaseResponse<Map<String, Object>> getNotices(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize) {

        NoticeSearchRequest noticeSearchRequest = NoticeSearchRequest.builder()
                .keyword(keyword)
                .category(category)
                .page(currentPage)
                .pageSize(pageSize)
                .build();

        Map<String, Object> result = noticeService.getNotices(noticeSearchRequest);
        return BaseResponse.success(result);
    }

}
