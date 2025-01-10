package com.checkping.dto.member.request;

import com.checkping.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateDto {
    private String name;
    private String phoneNum;
    private String jobRole;
    private String jobTitle;
    private String introduction;
    private String remark;

    public static void toEntity(Member member, MemberUpdateDto dto) {
        member.updateMemberInfo(
                dto.getName(),
                dto.getPhoneNum(),
                dto.getJobRole(),
                dto.getJobTitle(),
                dto.getIntroduction(),
                dto.getRemark()
        );
    }
}
