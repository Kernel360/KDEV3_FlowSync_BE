package com.checkping.api.auth.util;

import jakarta.servlet.http.Cookie;

public class CookieUtil {

    public static Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
//        cookie.setDomain("flowssync.com");

        return cookie;
    }

    public static Cookie deleteCookie(String key) {

        Cookie cookie = new Cookie(key, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
//        cookie.setDomain("flowssync.com");

        return cookie;
    }
}
