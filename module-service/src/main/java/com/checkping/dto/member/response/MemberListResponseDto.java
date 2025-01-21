package com.checkping.dto.member.response;

import com.checkping.domain.member.Member;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MemberListResponseDto {

    private final List<MemberResponseDto> members;
    private long totalElements;
    private int totalPages;

    public MemberListResponseDto(List<MemberResponseDto> members) {
        this.members = members;
    }

    public static MemberListResponseDto fromEntityPage(Page<Member> page) {
        List<MemberResponseDto> memberDtos = page.getContent().stream()
                .map(MemberResponseDto::fromEntity)
                .toList();
        return new MemberListResponseDto(memberDtos, page.getTotalElements(), page.getTotalPages());
    }

    // 생성자와 Getter 추가
    public MemberListResponseDto(List<MemberResponseDto> members, long totalElements, int totalPages) {
        this.members = members;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<MemberResponseDto> getMembers() {
        return members;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
