package com.checkping.service.notice;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.domain.notice.Notice;
import com.checkping.domain.project.Project;
import com.checkping.dto.notice.request.NoticeCreateRequestDto;
import com.checkping.dto.notice.request.NoticeUpdateRequestDto;
import com.checkping.dto.notice.response.NoticeCreateResponseDto;
import com.checkping.dto.notice.response.NoticeGetListResponseDto;
import com.checkping.dto.notice.response.NoticeUpdateResponseDto;
import com.checkping.infra.repository.notice.NoticeRepository;
import jakarta.persistence.Id;
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
    public NoticeCreateResponseDto registerNotice(NoticeCreateRequestDto noticeCreateRequestDto) {

        if (noticeCreateRequestDto.getAdminId() == null ||
                StringUtils.isBlank(noticeCreateRequestDto.getTitle()) ||
                StringUtils.isBlank(noticeCreateRequestDto.getContent()) ||
                noticeCreateRequestDto.getCategory() == null ||
                noticeCreateRequestDto.getPriority() == null) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        Notice notice = noticeRepository.save(noticeCreateRequestDto.toEntity());
        return NoticeCreateResponseDto.toDto(notice);
    }

    @Override
    @Transactional
    public NoticeUpdateResponseDto updateNotice(Long noticeid, NoticeUpdateRequestDto noticeUpdateRequestDto) {

        if(StringUtils.isBlank(noticeUpdateRequestDto.getTitle()) &&
                StringUtils.isBlank(noticeUpdateRequestDto.getContent()) &&
                noticeUpdateRequestDto.getCategory() == null &&
                noticeUpdateRequestDto.getPriority() == null){
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        Notice notice = noticeRepository.findById(noticeid)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        notice.updateNotice(noticeUpdateRequestDto.getTitle(), noticeUpdateRequestDto.getContent(), noticeUpdateRequestDto.getCategory(), noticeUpdateRequestDto.getPriority());

        return NoticeUpdateResponseDto.toDto(notice);

    }

    @Override
    public NoticeUpdateResponseDto deleteNotice(Long noticeid){

        Notice notice = noticeRepository.findById(noticeid)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        noticeRepository.deleteById(noticeid);

        return NoticeUpdateResponseDto.toDto(notice);
    }

    @Override
    public List<NoticeGetListResponseDto> findAllNotices(){
        List<Notice> result = noticeRepository.findAll();

        return result.stream()
                .map(NoticeGetListResponseDto::toDto)
                .collect(Collectors.toList());
    }
}
