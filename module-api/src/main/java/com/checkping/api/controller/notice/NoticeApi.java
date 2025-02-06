package com.checkping.api.controller.notice;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.notice.request.NoticeCreateRequest;
import com.checkping.dto.notice.request.NoticeSearchRequest;
import com.checkping.dto.notice.request.NoticeUpdateRequest;
import com.checkping.dto.notice.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Notice API(NoticeController)", description = "공지사항 API 입니다.")
public interface NoticeApi {

    @Operation(summary = "공지사항 생성", description = "공지사항을 생성하는 기능입니다.")
    public BaseResponse<NoticeCreateResponse> registerNotice(@Parameter(description = "생성할 공지사항 정보 Dto") NoticeCreateRequest noticeCreateRequest);

    @Operation(summary = "공지사항 수정", description = "공지사항을 수정하는 기능입니다.")
    public BaseResponse<NoticeResponse> updateNotice(
            @Parameter(description = "수정할 공지사항 아이디") Long noticeid,
            @Parameter(description = "수정할 공지사항 정보") NoticeUpdateRequest noticeUpdateRequest);

    @Operation(summary = "공지사항 삭제", description = "공지사항을 삭제하는 기능입니다.")
    public BaseResponse<NoticeResponse> deleteNotice(
            @Parameter(description = "삭제할 공지사항 아이디") Long noticeid
    );

    @Operation(summary = "공지사항 조회", description = "특정 공지사항을 조회하는 기능입니다")
    public BaseResponse<NoticeWithoutIsdeletedResponse> getNotice(
            @Parameter(description = "공지사항 아이디") Long noticeid
    );

    @Operation(summary = "공지사항 검색", description = "키워드와 카테고리로 공지사항을 검색하는 기능입니다.")
    public BaseResponse<NoticeListResponse> getNotices(
            @Parameter(description = "검색할 키워드") String keyword,
            @Parameter(description = "검색할 카테고리") String category,
            @Parameter(description = "검색할 페이지") int currentPage,
            @Parameter(description = "한 페이지에 보이는 공지사항 갯수") int pageSize
        );

}