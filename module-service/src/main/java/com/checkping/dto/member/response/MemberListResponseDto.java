package com.checkping.dto.member.response;

import com.checkping.common.response.PageMetaResponse;
import com.checkping.domain.member.Member;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class MemberListResponseDto {

    private final List<MemberResponseDto> members;
    private final PageMetaResponse meta;

    public MemberListResponseDto(List<MemberResponseDto> members, PageMetaResponse meta) {
        this.members = members;
        this.meta = meta;
    }

    public static MemberListResponseDto fromEntityPage(Page<Member> page) {
        List<MemberResponseDto> memberDtos = page.getContent().stream()
                .map(MemberResponseDto::fromEntity)
                .toList();

        PageMetaResponse meta = PageMetaResponse.fromPage(page);

        return new MemberListResponseDto(memberDtos, meta);
    }
}