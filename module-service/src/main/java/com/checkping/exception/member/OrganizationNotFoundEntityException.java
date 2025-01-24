package com.checkping.exception.member;

import com.checkping.common.enums.ErrorCode;

public class OrganizationNotFoundEntityException extends OrganizationException {

    public OrganizationNotFoundEntityException() {
        super("업체를 찾을 수 없습니다.", ErrorCode.NOT_FOUND);
    }
}
