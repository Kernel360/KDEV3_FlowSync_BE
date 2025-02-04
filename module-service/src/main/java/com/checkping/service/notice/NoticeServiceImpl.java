package com.checkping.service.notice;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.domain.notice.Notice;
import com.checkping.dto.notice.request.NoticeCreateRequest;
import com.checkping.dto.notice.request.NoticeSearchRequest;
import com.checkping.dto.notice.request.NoticeUpdateRequest;
import com.checkping.dto.notice.response.NoticeCreateResponse;
import com.checkping.dto.notice.response.NoticeGetListResponse;
import com.checkping.dto.notice.response.NoticeListResponse;
import com.checkping.dto.notice.response.NoticeResponse;
import com.checkping.infra.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public NoticeCreateResponse registerNotice(NoticeCreateRequest noticeCreateRequest) {

            Notice notice = noticeRepository.save(noticeCreateRequest.toEntity());
            return NoticeCreateResponse.toDto(notice);
    }

    @Override
    @Transactional
    public NoticeResponse updateNotice(Long noticeid, NoticeUpdateRequest noticeUpdateRequest) {

        Notice updateNotice = noticeRepository.findById(noticeid)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        if (updateNotice.getIsDeleted()) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        updateNotice.updateNotice(
                noticeUpdateRequest.getTitle(),
                noticeUpdateRequest.getContent() != null ? noticeUpdateRequest.convertContentToJson() : null,
                noticeUpdateRequest.getCategory(),
                noticeUpdateRequest.getPriority()
        );

        return NoticeResponse.toDto(updateNotice);
    }

    @Override
    public NoticeResponse deleteNotice(Long noticeid) {

        Notice notice = noticeRepository.findByIdAndIsDeletedFalse(noticeid)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        if (notice.getIsDeleted()) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        notice.markAsDeleted();

        noticeRepository.save(notice);

        return NoticeResponse.toDto(notice);
    }

    @Override
    public NoticeResponse getNotice(Long noticeid) {
        Notice notice = noticeRepository.findByIdAndIsDeletedFalse(noticeid)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        return NoticeResponse.toDto(notice);
    }

    @Override
    public NoticeListResponse getNotices(NoticeSearchRequest noticeSearchRequest) {
        int pageNumber = noticeSearchRequest.getPage() > 0 ? noticeSearchRequest.getPage() - 1 : 0;
        int pageSize = noticeSearchRequest.getPageSize() > 0 ? noticeSearchRequest.getPageSize() : 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        String keyword = noticeSearchRequest.getKeyword();
        Notice.Category category = null;

        String categoryStr = noticeSearchRequest.getCategory();
        if (categoryStr != null && !categoryStr.isBlank()) {
            try {
                category = Notice.Category.valueOf(categoryStr);
            } catch (IllegalArgumentException e) {
                throw new BaseException(ErrorCode.BAD_REQUEST);
            }
        }

        Page<Notice> result = noticeRepository.findSortedNotices(keyword, category, pageable);

        return NoticeListResponse.fromEntityPage(result);
    }

}

//TODO : 모든 DTO, 엔티티에서 관리자아이디 제거 (DB에서도 해당 컬럼 전부 제거)
//TODO : 예외처리를 포함한 리팩토링
//TODO : 모든 컬럼을 동일하게 수정 시 수정 불가 예외처리