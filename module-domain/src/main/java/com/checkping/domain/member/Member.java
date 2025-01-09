package com.checkping.domain.member;

import com.checkping.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Member extends BaseEntity {
    /*
      id : 회원아이디
      org_id : 소속 업체 아이디
      type : 회원 유형 * ADMIN/MEMBER
      status : 회원 상태 * ACTIVE/ INACTIVE
      email : 이메일
      pw : 비밀번호
      name : 회원 이름
      phone_num : 전화번호
      job_role : 직무
      jpb_title : 직급
      reg_at : 등록 일시
      introduction : 자기소개
      login_fail_count : 로그인 실패 횟수 * 최대 5회
      is_login_locked_yn ; 로그인 잠금 여부  * Y/N
      last_login_at : 최근 접속 일시
      delete_account_at : 회원 탈퇴 일시
      reason_for_delete_account : 탈퇴 사유
      pw_change_at : 비밀번호 변경일시
      profile_image_link : 프로필 이미지 링크
      remark : 비고
     */

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID id; // 회원 아이디

    @ManyToOne
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization; // 소속 업체 아이디

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type; // 회원 유형 ADMIN/MEMBER

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false, length = 2083)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "phone_num", nullable = false, length = 20)
    private String phoneNum;

    @Column(name = "job_role", length = 100)
    private String jobRole;

    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @CreatedDate
    @Column(name = "reg_at", updatable = false, nullable = false)
    private LocalDateTime regAt;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(name = "login_fail_count", nullable = false)
    private int loginFailCount;

    @Column(name = "is_login_locked_yn", nullable = false, length = 1)
    private char isLoginLockedYn;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "delete_account_at")
    private LocalDateTime deleteAccountAt;

    @Column(name = "reason_for_delete_account", length = 100)
    private String reasonForDeleteAccount;

    @Column(name = "pw_change_at")
    private LocalDateTime pwChangeAt;

    @Column(name = "profile_image_url", columnDefinition = "TEXT")
    private String profileImageUrl;

    @Column(length = 255)
    private String remark;

    public enum Type {
        ADMIN, MEMBER
    }

    public enum Status {
        ACTIVE, INACTIVE
    }
}

