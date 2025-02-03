package com.checkping.dto.member.response;

import com.checkping.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class MemberSignatureResponseDto {
    /*
    id : 회원아이디
    name : 회원이름
    url : 회원 사인 이미지 URL
     */

    @Schema(description = "회원 ID", example = "1")
    private Long id;
    @Schema(description = "회원 이름", example = "홍길동")
    private String name;
    @Schema(description = "회원 사인 이미지 URL", example = "https://www.example.com/signature.jpg")
    private String url;

    /**
     * Member Entity -> MemberSignatureResponseDto
     *
     * @param member Member Entity
     * @return MemberSignatureResponseDto
     */
    public static MemberSignatureResponseDto toDto(Member member) {
        MemberSignatureResponseDto dto = new MemberSignatureResponseDto();
        dto.id = member.getId();
        dto.name = member.getName();
        dto.url = member.getSignatureUrl();
        return dto;
    }

}
