package com.checkping.service.member.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class RedisConnectionCheckService {

    private final RedisTemplate<String, String> redisTemplate;

    public boolean isRedisAvailable() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executor.submit(() -> {
            try {
                String pong = redisTemplate.getConnectionFactory().getConnection().ping();
                return "PONG".equalsIgnoreCase(pong);
            } catch (Exception e) {
                return false;
            }
        });

        try {
            return future.get(500, TimeUnit.MILLISECONDS); // 0.5초 이상 걸리면 false 반환
        } catch (TimeoutException e) {
            System.out.println("[RedisConnectionCheck] Redis connection timeout -> Skip blacklist check");
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            executor.shutdown();
        }
    }
}
