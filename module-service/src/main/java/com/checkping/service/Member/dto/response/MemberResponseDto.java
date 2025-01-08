package com.checkping.service.Member.dto.response;

import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
public class MemberResponseDto {

    private UUID id; // 회원 ID
    private UUID organizationId; // 소속 업체 ID
    private String type; // 회원 유형 (ADMIN/MEMBER)
    private String status; // 회원 상태 (ACTIVE/INACTIVE)
    private String name; // 회원 이름
    private String email; // 이메일
    private String phoneNum; // 전화번호
    private String jobRole; // 직무
    private String jobTitle; // 직급
    private String remark; // 비고

    public static MemberResponseDto fromEntity(Member member) {
        Organization organization = member.getOrganization();
        return MemberResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .phoneNum(member.getPhoneNum())
                .jobRole(member.getJobRole())
                .jobTitle(member.getJobTitle())
                .type(member.getType().toString())
                .status(member.getStatus().toString())
                .organizationId(organization != null ? organization.getId() : null)
                .remark(member.getRemark())
                .build();
    }
}