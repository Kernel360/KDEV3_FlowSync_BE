package com.checkping.api.auth.config;

import com.checkping.api.auth.interceptor.DuplicatedClickPrevent;
import com.checkping.service.member.auth.RedisConnectionCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorMvcConfig implements WebMvcConfigurer {

    private final RedisConnectionCheckService redisConnectionCheckService;
    private final DuplicatedClickPrevent duplicatedClickPrevent;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // Redis 서버가 정상적으로 작동 중일 때만 중복 클릭 방지 인터셉터를 등록
        if(redisConnectionCheckService.isRedisAvailable()){
            registry.addInterceptor(duplicatedClickPrevent)
                    .addPathPatterns("/**") // 모든 경로에 인터셉터 적용, 예외 경로는 excludePathPatterns()로 설정
                    .excludePathPatterns(
                            "/login",
                            "/reissue",
                            "/swagger-ui/**",
                            "/v3/api-docs/**"
                    );
        }
    }
}
