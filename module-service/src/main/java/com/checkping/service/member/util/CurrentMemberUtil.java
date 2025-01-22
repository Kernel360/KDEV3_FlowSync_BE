package com.checkping.service.member.util;

import com.checkping.common.enums.ErrorCode;
import com.checkping.domain.member.Member;
import com.checkping.exception.member.MemberException;
import com.checkping.infra.repository.member.MemberRepository;
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
     * 현재 인증된 사용자의 이메일을 가져옵니다.
     *
     * @return 인증된 사용자의 이메일
     * @throws MemberException 사용자가 인증되지 않은 경우 예외 발생
     */
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new MemberException("사용자가 인증되지 않았습니다.", ErrorCode.UNAUTHORIZED);
        }
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();


        return authentication.getName(); // 토큰에서 추출된 이메일
    }

    /**
     * 현재 인증된 사용자의 정보를 반환합니다 (DTO).
     *
     * @return 현재 인증된 사용자의 MemberResponseDto
     * @throws MemberException 인증된 사용자를 찾을 수 없는 경우 예외 발생
     */
    public Member getCurrentMember() {
        String email = getCurrentUserEmail();
//        Optional<Member> result = memberRepository.findByEmail(email);// 현재 인증된 사용자의 이메일을 가져옴

        // 1/22 테스트용 코드 (관리자 계정으로 로그인) //TODO: 추후 삭제
        Optional<Member> result = memberRepository.findByEmail("admin@example.com");
        return result
                .orElseThrow(() -> new MemberException("사용자를 찾을 수 없습니다: " + email, ErrorCode.USER_NOT_FOUND));
    }
}