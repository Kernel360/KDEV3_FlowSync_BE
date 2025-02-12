package com.checkping.exception.project;

import com.checkping.common.enums.ErrorCode;

public class ProjectNotDevOwnerException extends ProjectException {

    public ProjectNotDevOwnerException() {
        super("해당 프로젝트의 개발사 최고 담당자가 아닙니다. 권한을 확인해주세요.", ErrorCode.FORBIDDEN);
    }
}
