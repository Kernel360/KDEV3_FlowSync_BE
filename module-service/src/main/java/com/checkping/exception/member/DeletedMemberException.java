package com.checkping.exception.member;

import com.checkping.common.enums.ErrorCode;

public class DeletedMemberException extends MemberException{

    public DeletedMemberException() {
        super("삭제된 회원입니다.", ErrorCode.DELETED_MEMBER);
    }
}
