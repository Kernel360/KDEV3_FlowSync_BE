package com.checkping.service.Member.dto.response;

import com.checkping.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberListResponseDto {

    private List<MemberResponseDto> members;

    public static MemberListResponseDto fromEntityList(List<Member> memberList) {
        return MemberListResponseDto.builder()
                .members(memberList.stream()
                        .map(MemberResponseDto::fromEntity)
                        .toList())
                .build();
    }
}