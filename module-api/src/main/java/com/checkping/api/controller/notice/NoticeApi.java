package com.checkping.api.controller.notice;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.notice.request.NoticeCreateRequest;
import com.checkping.dto.notice.request.NoticeUpdateRequest;
import com.checkping.dto.notice.response.NoticeCreateResponse;
import com.checkping.dto.notice.response.NoticeGetListResponse;
import com.checkping.dto.notice.response.NoticeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Notice API(NoticeController)", description = "공지사항 API 입니다.")
public interface NoticeApi {

    @Operation(summary = "공지사항 생성", description = "공지사항을 생성하는 기능입니다.")
    public BaseResponse<NoticeCreateResponse> registerNotice(@Parameter(description = "생성할 공지사항 정보 Dto") NoticeCreateRequest noticeCreateRequest);

    @Operation(summary = "공지사항 수정", description = "공지사항을 수정하는 기능입니다.")
    public BaseResponse<NoticeResponse> updateNotice(
            @Parameter(description = "수정할 공지사항 아이디") Long noticeid,
            @Parameter(description = "수정할 공지사항 정보Dto") NoticeUpdateRequest noticeUpdateRequest);

    @Operation(summary = "공지사항 삭제", description = "공지사항을 삭제하는 기능입니다.")
    public BaseResponse<NoticeResponse> deleteNotice(
            @Parameter(description = "공지사항 아이디") Long noticeid
    );

    @Operation(summary = "공지사항 목록 조회", description = "공지사항 목록을 조회하는 기능입니다.")
    public BaseResponse<List<NoticeGetListResponse>> findAllNotices();

    @Operation(summary = "공지사항 조회", description = "특정 공지사항을 조회하는 기능입니다")
    public BaseResponse<NoticeResponse> getNotice(
            @Parameter(description = "공지사항 아이디") Long noticeid
    );
}