package com.checkping.dto.member.response;

import com.checkping.domain.member.Member;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MemberListResponseDto {

    private final List<MemberResponseDto> members;

    public MemberListResponseDto(List<MemberResponseDto> members) {
        this.members = members;
    }

    public static MemberListResponseDto fromEntityList(List<Member> memberList) {
        List<MemberResponseDto> dtoList = memberList.stream()
                .map(MemberResponseDto::fromEntity)
                .collect(Collectors.toList());
        return new MemberListResponseDto(dtoList);
    }
}
