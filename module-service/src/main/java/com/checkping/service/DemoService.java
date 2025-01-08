package com.checkping.service;

import com.checkping.common.enums.ErrorCode;
import com.checkping.common.exception.CustomException;
import com.checkping.infra.repository.MemberRepository;
import com.checkping.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DemoService {

//    @Value("${profile-name}")
    private String name;

    private final MemberRepository memberRepository;

    public String save() {
        System.out.println("name : " + name);
        memberRepository.save(Member.builder()
                .name(Thread.currentThread().getName())
                .build());
        return "save";

    }

    public String find() {
        int size = memberRepository.findAll().size();
        log.info("DB size : " + size);
        return "find";
    }

    public String exception() throws Exception {
        if (true)
            throw new Exception();
        return "exception";
    }

    public String customException() {
        if (true)
            throw new CustomException(ErrorCode.BAD_REQUEST);
        return "exception";
    }
}
