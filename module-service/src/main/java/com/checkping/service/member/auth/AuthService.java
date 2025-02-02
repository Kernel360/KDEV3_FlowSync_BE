package com.checkping.service.member.auth;

import com.checkping.dto.member.response.MemberResponseDto;
import com.checkping.service.member.util.CurrentMemberUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final CurrentMemberUtil currentMemberUtil;
    public AuthService(CurrentMemberUtil currentMemberUtil) {
        this.currentMemberUtil = currentMemberUtil;
    }

    public MemberResponseDto.MeResponseDto getCurrentMember() {
        return MemberResponseDto.MeResponseDto.fromEntity(currentMemberUtil.getCurrentMember());
    }
}
