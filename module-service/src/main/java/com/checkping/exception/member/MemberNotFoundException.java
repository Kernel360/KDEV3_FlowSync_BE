package com.checkping.exception.member;

import com.checkping.common.enums.ErrorCode;

public class MemberNotFoundException extends MemberException{

    public MemberNotFoundException() {
        super("회원이 존재하지 않습니다.", ErrorCode.USER_NOT_FOUND);
    }
}
