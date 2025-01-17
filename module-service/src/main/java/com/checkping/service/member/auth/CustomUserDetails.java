package com.checkping.service.member.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private String name;
    private String email;
    private String role;
    private String password;

    public CustomUserDetails(String name, String email, String role, String password) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 목록
        List<GrantedAuthority> authorities = new ArrayList<>();
        // ROLE_USER, ROLE_ADMIN 형식
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
