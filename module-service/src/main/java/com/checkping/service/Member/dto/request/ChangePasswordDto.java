package com.checkping.service.Member.dto.request;

import com.checkping.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {

    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;

    // 비밀번호를 엔티티에 적용하는 static 메서드
    public static void updatePassword(Member member, String encodedPassword) {
        member.setPassword(encodedPassword);
    }
}
