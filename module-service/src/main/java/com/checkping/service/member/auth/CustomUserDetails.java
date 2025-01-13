package com.checkping.service.member.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {


    private String email;
    private String role;
    private String password;

    public CustomUserDetails(String email, String role, String password) {
        this.email = email;
        this.role = role;
        this.password = password;
    }
//    private final Member member;
//
//    public CustomUserDetails(Member member) {
//
//        this.member = member;
//    }


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//
//        Collection<GrantedAuthority> collection = new ArrayList<>();
//
//        collection.add(new GrantedAuthority() {
//
//            @Override
//            public String getAuthority() {
//
//                return member.getRole();
//            }
//        });
//
//        return collection;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Member 엔티티의 role 필드를 GrantedAuthority로 변환
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role)); // ROLE_ADMIN, ROLE_MEMBER 형식
        return authorities;
    }

    @Override
    public String getPassword() {

        return password;
    }

    // 이메일 getter
    @Override
    public String getUsername() {

        return email;
    }

    // TODO 아래 조건들 검토 후 수정
    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }
}
