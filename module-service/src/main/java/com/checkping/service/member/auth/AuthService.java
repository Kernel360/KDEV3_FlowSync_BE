package com.checkping.service.member.auth;

import com.checkping.common.response.BaseResponse;
import com.checkping.dto.member.response.MemberResponseDto;
import com.checkping.service.member.util.CurrentMemberUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final CurrentMemberUtil currentMemberUtil;
    public AuthService(CurrentMemberUtil currentMemberUtil) {
        this.currentMemberUtil = currentMemberUtil;
    }

    public BaseResponse getCurrentMember() {
//        return MemberResponseDto.fromEntity(currentMemberUtil.getCurrentMember());
    return BaseResponse.success(MemberResponseDto.fromEntity(currentMemberUtil.getCurrentMember()));
    }
}
