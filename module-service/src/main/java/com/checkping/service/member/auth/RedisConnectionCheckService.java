package com.checkping.service.member.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisConnectionCheckService {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    public boolean isRedisAvailable() {

        // Redis 연결 확인 로직
        // redisHost가 localhost인 경우에 false 반환
        if (redisHost.equals("localhost")) {
            return false;
        }

        return true;
    }
}
