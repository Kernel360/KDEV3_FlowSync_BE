package com.checkping.api.controller.notice;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.ProjectRequest;
import com.checkping.dto.ProjectResponse;
import com.checkping.dto.notice.request.NoticeCreateRequestDto;
import com.checkping.dto.notice.response.NoticeResponseDto;
import com.checkping.service.notice.NoticeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController {

    @Autowired
    private NoticeServiceImpl noticeService;

    @PostMapping("/admins/notices")
    public BaseResponse<NoticeResponseDto> resisterProjects(@RequestBody NoticeCreateRequestDto noticeCreateRequestDto) {
        NoticeResponseDto noticeResponseDto  = noticeService.registerNotice(noticeCreateRequestDto);
        return BaseResponse.success(noticeResponseDto);
    }
}
