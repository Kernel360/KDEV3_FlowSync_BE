package com.checkping.dto.member.response;

import com.checkping.domain.member.Member;
import lombok.Getter;

@Getter
public class MemberSignatureExistResponseDto {
    /*
    hasSignatures : 회원 사인 이미지 URL 존재 여부
    signatureUrl : 회원 사인 이미지 URL
     */
    private boolean hasSignatures;
    private String signatureUrl;

    /**
     * Member Entity -> MemberSignatureExistResponseDto
     *
     * @param member Member Entity
     * @return MemberSignatureExistResponseDto
     */
    public static MemberSignatureExistResponseDto toDto(Member member) {
        MemberSignatureExistResponseDto dto = new MemberSignatureExistResponseDto();
        dto.hasSignatures = member.getSignatureUrl() != null && !member.getSignatureUrl().isEmpty();
        dto.signatureUrl = member.getSignatureUrl();
        return dto;
    }
}
