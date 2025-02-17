package com.checkping.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Slf4j
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    public String getCurrentMemberEmail() {
        Object email = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getAttribute("email");

        if (email instanceof String) {
            return (String) email;
        }

        log.error("요청한 MemberId를 찾을 수 없습니다.");
        return "Error : 요청한 회원의 MemberId를 찾을 수 없습니다.";
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        String memberEmail = getCurrentMemberEmail();
        return Optional.of(memberEmail);
    }
}
