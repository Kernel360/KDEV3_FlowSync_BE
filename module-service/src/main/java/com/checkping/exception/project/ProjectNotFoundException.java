package com.checkping.exception.project;

import com.checkping.common.enums.ErrorCode;

public class ProjectNotFoundException extends ProjectException
{
    public ProjectNotFoundException() {
        super("프로젝트를 찾을 수 없습니다.", ErrorCode.NOT_FOUND);
    }

}
