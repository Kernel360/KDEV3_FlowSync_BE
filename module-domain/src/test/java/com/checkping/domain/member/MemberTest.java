package com.checkping.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {
    @Test
    @DisplayName("비밀번호 변경 테스트")
    void changePasswordTest() {
        // given
        Member member = Member.builder()
                .password("oldPassword")
                .build();

        // when
        member.changePassword("newEncodedPassword");

        // then
        assertThat(member.getPassword()).isEqualTo("newEncodedPassword");
        assertThat(member.getPwChangeAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("로그인 실패 횟수 증가 테스트")
    void increaseLoginFailCountTest() {
        // given
        Member member = Member.builder()
                .loginFailCount(0)
                .build();

        // when
        member.increaseLoginFailCount();

        // then
        assertThat(member.getLoginFailCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("회원 탈퇴 처리 테스트")
    void deleteAccountTest() {
        // given
        Member member = Member.builder()
                .status(Member.Status.ACTIVE)
                .deleteAccountAt(null)
                .reasonForDeleteAccount(null)
                .build();

        // when
        member.deleteAccount("테스트 탈퇴 사유");

        // then
        assertThat(member.getStatus()).isEqualTo(Member.Status.INACTIVE);
        assertThat(member.getDeleteAccountAt()).isNotNull();
        assertThat(member.getReasonForDeleteAccount()).isEqualTo("테스트 탈퇴 사유");
    }
}
