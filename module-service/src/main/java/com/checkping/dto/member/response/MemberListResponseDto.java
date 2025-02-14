package com.checkping.dto.member.response;

import com.checkping.common.dto.PageMetaResponse;
import com.checkping.domain.member.Member;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public class MemberListResponseDto {

    private final List<MemberResponseDto> members;
    private final Map<String, Object> meta;

    public MemberListResponseDto(List<MemberResponseDto> members, Map<String, Object> meta) {
        this.members = members;
        this.meta = meta;
    }

    public static MemberListResponseDto fromEntityPage(Page<Member> page) {

        List<MemberResponseDto> memberDtos = page.isEmpty() ?
                Collections.emptyList() : page.getContent().stream()
                .map(MemberResponseDto::fromEntity)
                .toList();

        PageMetaResponse meta = PageMetaResponse.fromPage(page);
        Map<String, Object> result =  meta.toMap();

        return new MemberListResponseDto(memberDtos, result);
    }
}