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
        return getSortedNotices(null, null, pageable);
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
    public Page<NoticeGetListResponse> searchNotices(String keyword, String category, Pageable pageable) {
        Notice.Category categoryEnum = category != null ? Notice.Category.valueOf(category) : null;
        return getSortedNotices(keyword, categoryEnum, pageable);
    }

    // 공지사항을 정렬하여 가져오는 공통 메서드
    // 1. 긴급 공지를 먼저 가져옴 (최신순)
    // 2. 긴급 공지 중 10일이 지난 것은 일반 공지로 전환
    // 3. 일반 공지를 가져옴 (최신순)
    // 4. 긴급 + 일반 공지를 합쳐서 반환

    private Page<NoticeGetListResponse> getSortedNotices(String keyword, Notice.Category category, Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("regAt")));

        // 1. 긴급 공지 가져오기
        Page<Notice> emergencyNotices = (category != null)
                ? noticeRepository.findByCategoryAndPriorityAndTitleContainingOrContentContainingAndIsDeletedFalse(category, Notice.Priority.EMERGENCY, keyword, keyword, sortedPageable)
                : noticeRepository.findByPriorityAndTitleContainingOrContentContainingAndIsDeletedFalse(Notice.Priority.EMERGENCY, keyword, keyword, sortedPageable);

        // 2. 긴급 공지 중 10일 지난 것은 일반 공지로 변경
        emergencyNotices.getContent().forEach(notice -> {
            if (notice.getRegAt().isBefore(LocalDateTime.now().minusDays(10))) {
                notice.updatePriority(Notice.Priority.NORMAL);
            }
        });

        // 3. 일반 공지 가져오기
        Page<Notice> normalNotices = (category != null)
                ? noticeRepository.findByCategoryAndPriorityAndTitleContainingOrContentContainingAndIsDeletedFalse(category, Notice.Priority.NORMAL, keyword, keyword, sortedPageable)
                : noticeRepository.findByPriorityAndTitleContainingOrContentContainingAndIsDeletedFalse(Notice.Priority.NORMAL, keyword, keyword, sortedPageable);

        // 4. 긴급 공지 + 일반 공지를 합침
        List<Notice> allNotices = new ArrayList<>();
        allNotices.addAll(emergencyNotices.getContent());
        allNotices.addAll(normalNotices.getContent());

        return new PageImpl<>(allNotices, pageable, emergencyNotices.getTotalElements() + normalNotices.getTotalElements())
                .map(NoticeGetListResponse::toDto);
    }
}
