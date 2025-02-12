package com.checkping.service.notice;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.common.utils.FileRequest;
import com.checkping.domain.member.Member;
import com.checkping.domain.notice.Notice;
import com.checkping.dto.notice.request.NoticeCreateRequest;
import com.checkping.dto.notice.request.NoticeSearchRequest;
import com.checkping.dto.notice.request.NoticeUpdateRequest;
import com.checkping.dto.notice.response.*;
import com.checkping.infra.repository.file.S3FileRepositoryImpl;
import com.checkping.infra.repository.notice.NoticeRepository;
import com.checkping.service.member.util.CurrentMemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final CurrentMemberUtil currentMemberUtil;

    private final NoticeRepository noticeRepository;

    private final S3FileRepositoryImpl s3FileRepository;

    private final S3FileRepositoryImpl s3FileRepositoryImpl;

    @Override
    public NoticeCreateResponse registerNotice(NoticeCreateRequest noticeCreateRequest, MultipartFile file) {

        Notice.Priority priority = Notice.Priority.valueOf(noticeCreateRequest.getPriority());

        if (priority == Notice.Priority.EMERGENCY) {
            long emergencyNoticeCount = noticeRepository.countByPriorityAndIsDeletedFalse(Notice.Priority.EMERGENCY);
            if (emergencyNoticeCount >= 3) {
                throw new BaseException("긴급 공지사항은 최대 3개 등록 가능합니다", ErrorCode.BAD_REQUEST);
            }
        }


        if (file != null) {

            NoticeCreateRequest newNoticeCreateRequest = null;

            FileRequest fileRequest = s3FileRepository.uploadFile(file);

            newNoticeCreateRequest = NoticeCreateRequest.builder()
                    .title(noticeCreateRequest.getTitle())
                    .content(noticeCreateRequest.getContent())
                    .category(noticeCreateRequest.getCategory())
                    .priority(noticeCreateRequest.getPriority())
                    .noticeFileUrl(fileRequest.saveName() + "|" + fileRequest.url())
                    .build();

            Notice notice = noticeRepository.save(newNoticeCreateRequest.toEntity());

            return NoticeCreateResponse.toDto(notice);
        }
            Notice notice = noticeRepository.save(noticeCreateRequest.toEntity());

            return NoticeCreateResponse.toDto(notice);
    }

    @Override
    @Transactional
    public NoticeResponse updateNotice(Long noticeid, NoticeUpdateRequest noticeUpdateRequest) {

        Notice notice = noticeRepository.findById(noticeid)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        if (notice.getIsDeleted()) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        notice.updateNotice(
                noticeUpdateRequest.getTitle(),
                noticeUpdateRequest.getContent() != null ? noticeUpdateRequest.convertContentToJson() : null,
                noticeUpdateRequest.getCategory(),
                noticeUpdateRequest.getPriority()
        );

        return NoticeWithIsdeletedResponse.toDto(notice);
    }

    @Override
    public NoticeResponse deleteNotice(Long noticeid) {

        Notice notice = noticeRepository.findById(noticeid)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        if (notice.getIsDeleted()) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        notice.markAsDeleted();

        noticeRepository.save(notice);

        return NoticeWithIsdeletedResponse.toDto(notice);
    }

    @Override
    public NoticeResponse getNotice(Long noticeid) {
        Member currentMember = currentMemberUtil.getCurrentMember();
        boolean isAdmin = currentMember.getRole() == Member.Role.ADMIN;

        Notice notice;
        if (isAdmin) {
            notice = noticeRepository.findById(noticeid)
                    .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND)); // 관리자: 삭제된 공지사항도 볼 수 있음
            return NoticeWithIsdeletedResponse.toDto(notice); // 관리자: isDeleted 포함
        } else {
            notice = noticeRepository.findByIdAndIsDeletedFalse(noticeid)
                    .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND)); // 비관리자: 삭제된 공지사항은 볼 수 없음
            return NoticeWithoutIsdeletedResponse.toDto(notice); // 비관리자: isDeleted 제외
        }
    }

    @Override
    public NoticeListResponse getNotices(NoticeSearchRequest noticeSearchRequest) {
        Member currentMember = currentMemberUtil.getCurrentMember();
        boolean isAdmin = currentMember.getRole() == Member.Role.ADMIN;

        int pageNumber = noticeSearchRequest.getPage() > 0 ? noticeSearchRequest.getPage() - 1 : 0;
        int pageSize = noticeSearchRequest.getPageSize() > 0 ? noticeSearchRequest.getPageSize() : 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        String keyword = noticeSearchRequest.getKeyword();
        Notice.Category category = null;

        String categoryStr = noticeSearchRequest.getCategory();
        if (categoryStr != null && !categoryStr.isBlank()) {
            try {
                category = Notice.Category.valueOf(categoryStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BaseException(ErrorCode.BAD_REQUEST);
            }
        }

        Boolean isDeleted = noticeSearchRequest.getIsDeletedAsBoolean();

        // 관리자인 경우 삭제된 공지도 포함해서 조회
        Page<Notice> result;
        if (isAdmin) {
            // 🔹 관리자는 삭제 여부(isDeleted) 필터 적용
            result = noticeRepository.findSortedNotices(keyword, category, isDeleted, pageable);
        } else {
            // 🔹 일반 사용자는 기존 로직 유지 (삭제된 공지사항 제외)
            result = noticeRepository.findSortedNoticesWithoutIsDeleted(keyword, category, pageable);
        }

        return isAdmin
                ? NoticeListResponse.fromEntityPage(result, true)  // 관리자: isDeleted 포함
                : NoticeListResponse.fromEntityPage(result, false); // 비관리자: isDeleted 제외
    }

}