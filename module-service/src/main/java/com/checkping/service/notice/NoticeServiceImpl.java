package com.checkping.service.notice;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.domain.notice.Notice;
import com.checkping.dto.notice.request.NoticeCreateRequest;
import com.checkping.dto.notice.request.NoticeUpdateRequest;
import com.checkping.dto.notice.response.NoticeCreateResponse;
import com.checkping.dto.notice.response.NoticeGetListResponse;
import com.checkping.dto.notice.response.NoticeResponse;
import com.checkping.infra.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @org.springframework.transaction.annotation.Transactional
    public NoticeResponse updateNotice(Long noticeid, NoticeUpdateRequest noticeUpdateRequest) {

        Notice notice = noticeRepository.findById(noticeid)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        notice.updateNotice(noticeUpdateRequest.getTitle(), noticeUpdateRequest.getContent(), noticeUpdateRequest.getCategory(), noticeUpdateRequest.getPriority());

        return NoticeResponse.toDto(notice);

    }

    @Override
    public NoticeResponse deleteNotice(Long noticeid){

        Notice notice = noticeRepository.findByIdAndIsDeletedFalse(noticeid)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        if(notice.getIsDeleted()){
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        notice.markAsDeleted();

        noticeRepository.save(notice);

        return NoticeResponse.toDto(notice);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoticeGetListResponse> findAllNotices(Pageable pageable) {
        return noticeRepository.findAllByIsDeletedFalse(pageable)
                .map(NoticeGetListResponse::toDto);
    }

    @Override
    public NoticeResponse getNotice(Long noticeid) {

        Notice notice = noticeRepository.findByIdAndIsDeletedFalse(noticeid)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        return NoticeResponse.toDto(notice);
    }
}
