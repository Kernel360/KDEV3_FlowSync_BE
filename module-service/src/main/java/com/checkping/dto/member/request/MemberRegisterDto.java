package com.checkping.dto.member.request;

import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class MemberRegisterDto {

    private UUID organizationId;
    private String email;
    private String password;
    private String name;
    private String role;
    private String phoneNum;
    private String jobRole;
    private String jobTitle;
    private String introduction;
    private String remark;

    //DTO -> Member 엔티티 변환
    //- 여기서 넘어온 비밀번호는 이미 Service에서 암호화된 문자열
    public static Member toEntity(MemberRegisterDto dto, Organization organization, String encodedPassword) {
        return Member.builder()
                .organization(organization)
                .email(dto.getEmail())
                .password(encodedPassword)
                .name(dto.getName())
                .phoneNum(dto.getPhoneNum())
                .jobRole(dto.getJobRole())
                .jobTitle(dto.getJobTitle())
                .introduction(dto.getIntroduction())
                .remark(dto.getRemark())
                .role(Member.Role.valueOf(dto.getRole()))
                .status(Member.Status.ACTIVE) // 기본 값 ACTIVE
                .loginFailCount(0) // 기본 값 0
                .isLoginLockedYn('N') // 기본 값 N
                .build();
    }
}
