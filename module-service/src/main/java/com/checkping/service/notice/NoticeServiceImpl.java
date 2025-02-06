package com.checkping.service.notice;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.domain.member.Member;
import com.checkping.domain.notice.Notice;
import com.checkping.dto.notice.request.NoticeCreateRequest;
import com.checkping.dto.notice.request.NoticeSearchRequest;
import com.checkping.dto.notice.request.NoticeUpdateRequest;
import com.checkping.dto.notice.response.*;
import com.checkping.infra.repository.notice.NoticeRepository;
import com.checkping.service.member.util.CurrentMemberUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final CurrentMemberUtil currentMemberUtil;

    private final NoticeRepository noticeRepository;

    @Override
    public NoticeCreateResponse registerNotice(NoticeCreateRequest noticeCreateRequest) {

        Notice.Priority priority = Notice.Priority.valueOf(noticeCreateRequest.getPriority());

        if (priority == Notice.Priority.EMERGENCY) {
            long emergencyNoticeCount = noticeRepository.countByPriorityAndIsDeletedFalse(Notice.Priority.EMERGENCY);
            if (emergencyNoticeCount >= 3) {
                throw new BaseException(ErrorCode.BAD_REQUEST);
            }
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

        return NoticeResponse.toDto(notice);
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

        return NoticeResponse.toDto(notice);
    }

    @Override
    public Map<String, Object> getNotice(Long noticeid) {
        Member currentMember = currentMemberUtil.getCurrentMember();
        boolean isAdmin = currentMember.getRole()== Member.Role.ADMIN;

        Notice notice;

        if(isAdmin){
            notice = noticeRepository.findById(noticeid)
                    .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        } else {
            notice = noticeRepository.findByIdAndIsDeletedFalse(noticeid)
                    .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
        }

        NoticeResponse response = NoticeResponse.toDto(notice);

        Map<String, Object> responseMap = new ObjectMapper().convertValue(response, new TypeReference<>() {
        });

        if (!isAdmin){
            responseMap.remove("isDeleted");
        }

        return responseMap;
    }

    @Override
    public Map<String, Object> getNotices(NoticeSearchRequest noticeSearchRequest) {
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
                category = Notice.Category.valueOf(categoryStr);
            } catch (IllegalArgumentException e) {
                throw new BaseException(ErrorCode.BAD_REQUEST);
            }
        }

        Page<Notice> result;

        if (isAdmin) {
            // 관리자는 삭제된 공지 포함 조회
            result = noticeRepository.findSortedNotices(keyword, category, pageable);
        } else {
            // 일반 사용자는 삭제되지 않은 공지만 조회
            result = noticeRepository.findSortedNoticesForNonAdmin(keyword, category, pageable);
        }

        NoticeListResponse response = NoticeListResponse.fromEntityPage(result);

        Map<String, Object> responseMap = new ObjectMapper().convertValue(response, new TypeReference<>() {});

        if (!isAdmin) {
            List<Map<String, Object>> modifiedNotices = ((List<Map<String, Object>>) responseMap.get("notices"))
                    .stream()
                    .peek(notice -> notice.remove("isDeleted"))
                    .toList();
            responseMap.put("notices", modifiedNotices);
        }

        return responseMap;
    }
}

//TODO : 모든 DTO, 엔티티에서 관리자아이디 제거 (DB에서도 해당 컬럼 전부 제거)
//TODO : 모든 컬럼을 동일하게 수정 시 수정 불가 예외처리