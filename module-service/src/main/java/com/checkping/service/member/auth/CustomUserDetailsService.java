package com.checkping.service.member.auth;

import com.checkping.domain.member.Member;
import com.checkping.infra.repository.member.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //DB에서 조회
        Optional<Member> result = memberRepository.findByEmail(email);

        Member member = result.orElseThrow(() -> new UsernameNotFoundException(email));

        if (member != null) {

            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(member.getEmail(), member.getRole().toString(), member.getPassword());
        }

        return null;
    }
}
