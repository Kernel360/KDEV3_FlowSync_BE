package com.checkping.service.member.util;

import com.checkping.common.enums.ErrorCode;
import com.checkping.domain.member.Member;
import com.checkping.exception.member.MemberException;
import com.checkping.infra.repository.member.MemberRepository;
import com.checkping.service.member.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class CurrentMemberUtil {

    private final MemberRepository memberRepository;

    /**
     * 현재 인증된 사용자의 이메일을 반환
     *
     * @return 인증된 사용자의 이메일
     * @throws MemberException 사용자가 인증되지 않은 경우 예외 발생
     */
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new MemberException("사용자가 인증되지 않았습니다.", ErrorCode.UNAUTHORIZED);
        }
        Object principal = authentication.getPrincipal();
        String email;

        if (principal instanceof CustomUserDetails) {
            email = ((CustomUserDetails) principal).getUsername(); // getUsername()은 이메일을 반환한다고 가정
        } else if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        } else {
            throw new MemberException("인증된 사용자의 정보를 불러올 수 없습니다.", ErrorCode.USER_NOT_FOUND);
        }

        return email;
    }

    /**
     * 현재 인증된 사용자의 정보를 반환 (DTO).
     *
     * @return 현재 인증된 사용자의 MemberResponseDto
     * @throws MemberException 인증된 사용자를 찾을 수 없는 경우 예외 발생
     */
    public Member getCurrentMember() {
        String email = getCurrentUserEmail();

        Optional<Member> result = memberRepository.findByEmail(email);
        return result
                .orElseThrow(() -> new MemberException("사용자를 찾을 수 없습니다: " + email, ErrorCode.USER_NOT_FOUND));
    }
}