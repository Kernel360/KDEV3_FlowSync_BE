package com.checkping.service.notice;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.BaseException;
import com.checkping.dto.notice.request.NoticeCreateRequestDto;
import com.checkping.dto.notice.response.NoticeResponseDto;
import com.checkping.infra.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Override
    public NoticeResponseDto registerNotice(NoticeCreateRequestDto noticeCreateRequestDto) {

        if (noticeCreateRequestDto.getAdminId() == null ||
                StringUtils.isBlank(noticeCreateRequestDto.getTitle()) ||
                StringUtils.isBlank(noticeCreateRequestDto.getContent()) ||
                noticeCreateRequestDto.getCategory() == null ||
                noticeCreateRequestDto.getPriority() == null) {
            throw new BaseException(ErrorCode.BAD_REQUEST);
        }

        return NoticeResponseDto.toDto(noticeRepository.save(noticeCreateRequestDto.toEntity()));
    }
}
