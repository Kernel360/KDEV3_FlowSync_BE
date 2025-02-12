package com.checkping.api.controller.notice;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.notice.request.NoticeCreateRequest;
import com.checkping.dto.notice.request.NoticeSearchRequest;
import com.checkping.dto.notice.request.NoticeUpdateRequest;
import com.checkping.dto.notice.response.*;
import com.checkping.service.notice.NoticeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class NoticeController implements NoticeApi {

    private final NoticeServiceImpl noticeService;

    @Override
    @PostMapping(value = "/admins/notices", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<NoticeCreateResponse> registerNotice(@RequestPart NoticeCreateRequest noticeCreateRequest,
                                                             @RequestPart(required = false) MultipartFile file) {
        NoticeCreateResponse noticeCreateResponse = noticeService.registerNotice(noticeCreateRequest, file);
        return BaseResponse.success(noticeCreateResponse);
    }

    @Override
    @PutMapping(value = "/admins/notices/{noticeid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<NoticeResponse> updateNotice(
            @PathVariable Long noticeid,
            @RequestPart NoticeUpdateRequest noticeUpdateRequest,
            @RequestPart(required = false) MultipartFile file) {
        NoticeResponse noticeWithIsdeletedResponse = noticeService.updateNotice(noticeid, noticeUpdateRequest, file);
        return BaseResponse.success(noticeWithIsdeletedResponse);
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
    public BaseResponse<NoticeResponse> getNotice(
            @PathVariable Long noticeid
    ){
        NoticeResponse noticeGetResponse = noticeService.getNotice(noticeid);
        return BaseResponse.success(noticeGetResponse);
    }

    @Override
    @GetMapping("/notices")
    public BaseResponse<NoticeListResponse> getNotices(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String isDeleted){

        NoticeSearchRequest noticeSearchRequest = NoticeSearchRequest.builder()
                .keyword(keyword)
                .category(category)
                .page(currentPage)
                .pageSize(pageSize)
                .isDeleted(isDeleted)
                .build();

        NoticeListResponse result = noticeService.getNotices(noticeSearchRequest);
        return BaseResponse.success(result);
    }
}
