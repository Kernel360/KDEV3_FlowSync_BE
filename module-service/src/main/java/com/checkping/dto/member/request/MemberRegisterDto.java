package com.checkping.dto.member.request;

import com.checkping.domain.member.Member;
import com.checkping.domain.member.Organization;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class MemberRegisterDto {

    @Schema(description = "조직 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID organizationId;
    @Schema(description = "이메일", example = "example@example.com")
    private String email;
    @Schema(description = "비밀번호", example = "password1234")
    private String password;
    @Schema(description = "이름", example = "Optimus Prime")
    private String name;
    @Schema(description = "역할", example = "ADMIN") // ADMIN, USER
    private String role;
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNum;
    @Schema(description = "직무", example = "개발자")
    private String jobRole;
    @Schema(description = "직책", example = "팀장")
    private String jobTitle;
    @Schema(description = "소개", example = "안녕하세요. 저는 옵티머스프라임입니다.")
    private String introduction;
    @Schema(description = "비고", example = "곧 퇴사함")
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
