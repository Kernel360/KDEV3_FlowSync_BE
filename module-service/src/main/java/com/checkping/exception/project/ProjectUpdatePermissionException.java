package com.checkping.exception.project;

import com.checkping.common.enums.ErrorCode;

public class ProjectUpdatePermissionException extends ProjectException {

    public ProjectUpdatePermissionException() {
        super("프로젝트 수정 권한이 없습니다.", ErrorCode.BAD_REQUEST);
    }
}
