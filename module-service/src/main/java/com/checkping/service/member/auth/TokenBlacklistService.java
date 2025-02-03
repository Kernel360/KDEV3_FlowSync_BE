package com.checkping.service.member.auth;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 🔹 Access Token 블랙리스트 저장
    public void blacklistAccessToken(String token, long expiration) {
        redisTemplate.opsForValue().set("access_" + token, "blacklisted", expiration, TimeUnit.MILLISECONDS);
    }

    // 🔹 Refresh Token 블랙리스트 저장
    public void blacklistRefreshToken(String token, long expiration) {
        redisTemplate.opsForValue().set("refresh_" + token, "blacklisted", expiration, TimeUnit.MILLISECONDS);
    }

    // 🔹 Access Token 블랙리스트 확인
    public boolean isAccessTokenBlacklisted(String token) {
        return redisTemplate.hasKey("access_" + token);
    }

    // 🔹 Refresh Token 블랙리스트 확인
    public boolean isRefreshTokenBlacklisted(String token) {
        return redisTemplate.hasKey("refresh_" + token);
    }
}