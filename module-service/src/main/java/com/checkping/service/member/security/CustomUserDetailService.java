package com.checkping.service.member.security;

import com.checkping.domain.member.Member;
import com.checkping.infra.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("---------------------------loadUserByUsername Start--------------------------------");

        Optional<Member> result = memberRepository.findByEmail(username);

        Member member = result.orElseThrow(() -> new UsernameNotFoundException(username));

        MemberDto memberDto = new MemberDto(
                member.getEmail(),
                member.getPassword(),
                member.getRole().toString(),
                member.getName(),
                member.getPhoneNum(),
                member.getJobRole(),
                member.getJobTitle(),
                member.getIntroduction(),
                member.getOrganization().getId().toString()
        );

        log.info(memberDto.toString());
        log.info("---------------------------loadUserByUsername End--------------------------------");

        return memberDto;
    }
}
