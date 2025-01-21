package com.checkping.exception.member;

import com.checkping.common.enums.ErrorCode;

public class OrganizationAlreadyExistEntityException extends OrganizationException {

    public OrganizationAlreadyExistEntityException() {
        super("이미 존재하는 업체 입니다.", ErrorCode.DUPLICATE_RESOURCE);
    }
}
