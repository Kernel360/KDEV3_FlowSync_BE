package com.checkping.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    // 0번 저장소 사용 - 토큰 블랙리스트
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    // 1번 저장소 사용 - 비활성화 회원 목록
    @Bean
    public RedisConnectionFactory redisConnectionFactoryForInactiveMembers() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisHost, redisPort);
        factory.setDatabase(1);
        factory.afterPropertiesSet();  // 필수 초기화 호출
        return factory;
    }

    // 2번 저장소 사용 - 중복 클릭 방지
    @Bean
    public RedisConnectionFactory redisConnectionFactoryForDuplicatedClick() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisHost, redisPort);
        factory.setDatabase(2);
        factory.afterPropertiesSet();  // 필수 초기화 호출
        return factory;
    }

    @Bean // 0번 저장소 사용
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean // 1번 저장소 사용
    @Primary
    public StringRedisTemplate redisTemplateForInactiveMembers() {
        return new StringRedisTemplate(redisConnectionFactoryForInactiveMembers());
    }

    @Bean // 2번 저장소 사용 (중복 클릭 방지)
    public StringRedisTemplate redisTemplateForDuplicatedClick() {
        return new StringRedisTemplate(redisConnectionFactoryForDuplicatedClick());
    }
}