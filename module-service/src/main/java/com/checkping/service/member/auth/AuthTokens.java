package com.checkping.service.member.auth;

/**
 * access/refresh 토큰 묶음
 */
public class AuthTokens {
    private final String access;
    private final String refresh;

    public AuthTokens(String access, String refresh) {
        this.access = access;
        this.refresh = refresh;
    }

    public String getAccess() {
        return access;
    }

    public String getRefresh() {
        return refresh;
    }
}