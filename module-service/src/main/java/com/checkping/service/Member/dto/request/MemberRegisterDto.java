package com.checkping.service.Member.dto.request;


import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegisterDto {

    @NotBlank(message = " 소속 업체를 반드시 입력해야합니다. ")
    private UUID organizationId; // 소속 업체 아이디
    @NotBlank(message = " ADMIN/ MEMBER ")
    private String type; // 회원 유형 ADMIN/MEMBER
    @NotBlank(message = " ACTIVE/ INACTIVE")
    private String status; // 회원 상태 ACTIVE/INACTIVE
    @NotBlank
    private String email; // 이메일
    @NotBlank
    private String password; // 비밀번호
    @NotBlank
    private String name; // 회원 이름
    @NotBlank
    private String phoneNum; // 전화번호
    private String jobRole; // 직무
    private String jobTitle; // 직급
    private String introduction;//자기소개
    private String profile_image_link;//프로필 이미지 링크
    private String remark;// 비고



    public static Member toEntity(MemberRegisterDto dto, Organization organization, String encodedPassword) {
        return Member.builder()
                .email(dto.getEmail())
                .password(encodedPassword) // 암호화된 비밀번호 사용
                .name(dto.getName())
                .phoneNum(dto.getPhoneNum())
                .jobRole(dto.getJobRole())
                .jobTitle(dto.getJobTitle())
                .organization(organization)
                .type(Member.Type.valueOf(dto.getType()))
                .status(Member.Status.valueOf(dto.getStatus()))
                .introduction(dto.getIntroduction())
                .remark(dto.getRemark())
                .profileImageUrl(dto.profile_image_link)
                .build();
    }
}




