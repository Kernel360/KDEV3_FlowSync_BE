package com.checkping.dto.member.response;

import com.checkping.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponseDto {

    @Schema(description = "회원 ID", example = "37")
    private Long id;
    @Schema(description = "소속 업체 ID", example = "1")
    private Long organizationId;
    @Schema(description = "소속 업체 이름", example = "CheckPing")
    private String organizationName;
    @Schema(description = "역할", example = "ADMIN") // ADMIN, USER
    private Member.Role role;
    @Schema(description = "회원 탈퇴 여부", example = "ACTIVE") // ACTIVE, INACTIVE
    private Member.Status status;
    @Schema(description = "이메일", example = "example@example.com")
    private String email;
    @Schema(description = "이름", example = "Optimus Prime")
    private String name;
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNum;
    @Schema(description = "직무", example = "개발자")
    private String jobRole;
    @Schema(description = "직급", example = "팀장")
    private String jobTitle;
    @Schema(description = "등록 일시", example = "2021-07-01 12:00:00")
    private String regAt;
    @Schema(description = "소개", example = "안녕하세요. 저는 옵티머스프라임입니다.")
    private String introduction;
    @Schema(description = "비고", example = "곧 퇴사함")
    private String remark;

    public static MemberResponseDto fromEntity(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .organizationId(member.getOrganization().getId())
                .organizationName(member.getOrganization().getName())
                .role(member.getRole())
                .status(member.getStatus())
                .email(member.getEmail())
                .name(member.getName())
                .phoneNum(member.getPhoneNum())
                .jobRole(member.getJobRole())
                .jobTitle(member.getJobTitle())
                .jobRole(member.getJobRole())
                .regAt(member.getRegAt().toString())
                .introduction(member.getIntroduction())
                .remark(member.getRemark())
                .build();
    }

    @Getter
    @Builder
    public static class MeResponseDto{
        @Schema(description = "회원 ID", example = "37")
        private Long id;
        @Schema(description = "역할", example = "ADMIN") // ADMIN, USER
        private Member.Role role;
        @Schema (description = "회원 이름", example = "홍길동")
        private String name;
        @Schema(description = "소속 업체 ID", example = "1")
        private Long organizationId;
        @Schema(description = "소속 업체 이름", example = "CheckPing")
        private String organizationName;
        @Schema(description = "소속 업체 유형", example = "DEVELOPER") // DEVELOPER, CUSTOMER
        private String organizationType;

        public static MeResponseDto fromEntity(Member member) {
            return MeResponseDto.builder()
                    .id(member.getId())
                    .role(member.getRole())  // 예: ADMIN / MEMBER
                    .name(member.getName())
                    .organizationId(member.getOrganization().getId())
                    .organizationName(member.getOrganization().getName())
                    .organizationType( member.getOrganization().getType().name())  // 예: DEVELOPER / CUSTOMER)
                    .build();
        }
    }
}