package com.checkping.api.auth.config;

import com.checkping.api.auth.interceptor.ApprovalAuthorizationInterceptor;
import com.checkping.api.auth.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final ApprovalAuthorizationInterceptor approvalAuthorizationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
        registry.addInterceptor(approvalAuthorizationInterceptor);
    }
}
