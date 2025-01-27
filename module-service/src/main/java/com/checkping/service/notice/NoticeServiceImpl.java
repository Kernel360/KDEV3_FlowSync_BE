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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Override
    public NoticeCreateResponse registerNotice(NoticeCreateRequest noticeCreateRequest) {

        if (noticeCreateRequest.getAdminId() == null ||
                StringUtils.isBlank(noticeCreateRequest.getTitle()) ||
                StringUtils.isBlank(noticeCreateRequest.getContent()) ||
                noticeCreateRequest.getCategory() == null ||
                noticeCreateRequest.getPriority() == null) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        Notice notice = noticeRepository.save(noticeCreateRequest.toEntity());
        return NoticeCreateResponse.toDto(notice);
    }

    @Override
    @Transactional
    public NoticeResponse updateNotice(Long noticeid, NoticeUpdateRequest noticeUpdateRequest) {

        if(StringUtils.isBlank(noticeUpdateRequest.getTitle()) &&
                StringUtils.isBlank(noticeUpdateRequest.getContent()) &&
                noticeUpdateRequest.getCategory() == null &&
                noticeUpdateRequest.getPriority() == null){
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

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
    public List<NoticeGetListResponse> findAllNotices(){
        List<Notice> result = noticeRepository.findAllByIsDeletedFalse();

        return result.stream()
                .map(NoticeGetListResponse::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public NoticeResponse getNotice(Long noticeid) {

        Notice notice = noticeRepository.findByIdAndIsDeletedFalse(noticeid)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        return NoticeResponse.toDto(notice);
    }
}
