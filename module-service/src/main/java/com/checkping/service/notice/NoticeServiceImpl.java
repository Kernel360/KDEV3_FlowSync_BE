package com.checkping.service.notice;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.domain.notice.Notice;
import com.checkping.dto.notice.request.NoticeCreateRequest;
import com.checkping.dto.notice.request.NoticeSearchRequest;
import com.checkping.dto.notice.request.NoticeUpdateRequest;
import com.checkping.dto.notice.response.NoticeCreateResponse;
import com.checkping.dto.notice.response.NoticeGetListResponse;
import com.checkping.dto.notice.response.NoticeResponse;
import com.checkping.infra.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

        Notice notice = noticeRepository.findById(noticeid)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        notice.updateNotice(noticeUpdateRequest.getTitle(), noticeUpdateRequest.getContent(), noticeUpdateRequest.getCategory(), noticeUpdateRequest.getPriority());

        return NoticeResponse.toDto(notice);

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
    @Transactional(readOnly = true)
    public NoticeResponse getNotice(Long noticeid) {
        Notice notice = noticeRepository.findByIdAndIsDeletedFalse(noticeid)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        return NoticeResponse.toDto(notice);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoticeGetListResponse> findAllNotices(int page) {
        int pageNumber = page > 0 ? page - 1 : 0;

        Pageable pageable = PageRequest.of(pageNumber, 10);

        return getSortedNotices(null, null, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoticeGetListResponse> searchNotices(NoticeSearchRequest noticeSearchRequest) {
        int pageNumber = noticeSearchRequest.getPage() > 0 ? noticeSearchRequest.getPage() - 1 : 0;
        Pageable pageable = PageRequest.of(pageNumber, 10);

        Notice.Category category = null;
        if (noticeSearchRequest.getCategory() != null) {
            category = Notice.Category.valueOf(noticeSearchRequest.getCategory());
        }
        return getSortedNotices(noticeSearchRequest.getKeyword(), category, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoticeGetListResponse> getSortedNotices(String keyword, Notice.Category category, Pageable pageable) {

        return noticeRepository.findSortedNotices(keyword, category, pageable)
                .map(NoticeGetListResponse::toDto);
    }

}
