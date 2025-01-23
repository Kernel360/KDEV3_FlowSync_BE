package com.checkping.dto.member.response;

import com.checkping.domain.member.Member;
import lombok.Getter;
import org.springframework.data.domain.Page;

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
        List<MemberResponseDto> memberDtos = page.getContent().stream()
                .map(MemberResponseDto::fromEntity)
                .toList();

        Map<String, Object> meta = Map.of(
                "totalElements", page.getTotalElements(),   // 전체 원소 개수
                "isFirstPage", page.isFirst(),              // 첫 번째 페이지 여부
                "isLastPage", page.isLast(),                // 마지막 페이지 여부
                "totalPages", page.getTotalPages(),         // 전체 페이지 개수
                "pageSize", page.getSize(),                 // 한 페이지당 원소 개수
                "currentPage", page.getNumber() + 1             // 현재 페이지 번호
        );

        return new MemberListResponseDto(memberDtos, meta);
    }
}