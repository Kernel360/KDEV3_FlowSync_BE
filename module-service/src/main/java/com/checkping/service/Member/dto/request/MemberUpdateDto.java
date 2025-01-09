package com.checkping.service.Member.dto.request;

import com.checkping.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateDto {
    @NotBlank(message = "회원 이름은 필수 입력 항목입니다.")
    @Size(max = 100, message = "회원 이름은 최대 100자까지 입력 가능합니다.")
    private String name;
    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    @Size(max = 20, message = "전화번호는 최대 20자까지 입력 가능합니다.")
    private String phoneNum;

    @Size(max = 100, message = "직무는 최대 100자까지 입력 가능합니다.")
    private String jobRole;

    @Size(max = 100, message = "직급은 최대 100자까지 입력 가능합니다.")
    private String jobTitle;

    @Size(max = 255, message = "비고는 최대 255자까지 입력 가능합니다.")
    private String remark;

    public static Member toEntity(Member member, MemberUpdateDto dto) {
        member.setName(dto.getName());
        member.setPhoneNum(dto.getPhoneNum());
        member.setJobRole(dto.getJobRole());
        member.setJobTitle(dto.getJobTitle());
        member.setRemark(dto.getRemark());
        return member;
    }
}
