package com.checkping.exception.project;

import com.checkping.common.enums.ErrorCode;

public class ProjectNotMemberException extends ProjectException {

    public ProjectNotMemberException() {
        super("해당 프로젝트에 속한 업체의 멤버가 아닙니다.", ErrorCode.BAD_REQUEST);
    }
}
