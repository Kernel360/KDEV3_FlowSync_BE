package com.checkping.dto.member.request;

import com.checkping.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateDto {
    @Schema(description = "이름", example = "Optimus Prime")
    private String name;
    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNum;
    @Schema(description = "직무", example = "디자이너")
    private String jobRole;
    @Schema(description = "직책", example = "팀장")
    private String jobTitle;
    @Schema(description = "소개", example = "안녕하세요. 저는 옵티머스프라임입니다.")
    private String introduction;
    @Schema(description = "비고", example = "곧 퇴사함")
    private String remark;

    public static Member toEntity(Member member, MemberUpdateDto dto) {
        member.updateMemberInfo(
                dto.getName(),
                dto.getPhoneNum(),
                dto.getJobRole(),
                dto.getJobTitle(),
                dto.getIntroduction(),
                dto.getRemark()
        );
        return member;
    }
}
