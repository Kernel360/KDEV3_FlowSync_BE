package com.checkping.dto.member.request;

import com.checkping.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ChangePasswordDto {

    @Schema(description = "현재 비밀번호", example = "password1234")
    private String currentPassword;
    @Schema(description = "새로운 비밀번호", example = "newPassword1234")
    private String newPassword;
    @Schema(description = "새로운 비밀번호 확인", example = "newPassword1234")
    private String confirmNewPassword;

    public static void updatePassword(Member member, String encodedPassword) {
        member.changePassword(encodedPassword);
    }
}