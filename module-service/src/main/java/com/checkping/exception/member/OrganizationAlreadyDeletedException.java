package com.checkping.exception.member;

import com.checkping.common.enums.ErrorCode;

public class OrganizationAlreadyDeletedException extends OrganizationException {
    public OrganizationAlreadyDeletedException() {
        super("삭제된 업체는 상태를 변경할 수 없습니다.", ErrorCode.BAD_REQUEST);
    }
}
