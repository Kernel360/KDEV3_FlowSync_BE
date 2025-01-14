package com.checkping.dto.member.response;

import com.checkping.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class MemberResponseDto {

    private UUID id;
    private UUID organizationId;
    private String email;
    private String name;
    private Member.Role role;
    private String phoneNum;
    private String jobRole;
    private String jobTitle;
    private Member.Status status;
    private int loginFailCount;
    private char isLoginLockedYn;

    public static MemberResponseDto fromEntity(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .organizationId(member.getOrganization().getId())
                .email(member.getEmail())
                .name(member.getName())
                .phoneNum(member.getPhoneNum())
                .role(member.getRole())
                .jobRole(member.getJobRole())
                .jobTitle(member.getJobTitle())
                .status(member.getStatus())
                .loginFailCount(member.getLoginFailCount())
                .isLoginLockedYn(member.getIsLoginLockedYn())
                .build();
    }
}
