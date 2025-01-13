package com.checkping.dto.member.request;

import com.checkping.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ChangePasswordDto {

    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;

    public static void updatePassword(Member member, String encodedPassword) {
        member.changePassword(encodedPassword);
    }
}