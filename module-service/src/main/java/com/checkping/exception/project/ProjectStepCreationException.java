package com.checkping.exception.project;

import com.checkping.common.enums.ErrorCode;

public class ProjectStepCreationException extends ProjectException {
    public ProjectStepCreationException() {
        super("프로젝트 단계 생성 중 오류가 발생했습니다.", ErrorCode.INTERNAL_SERVER_ERROR);
    }
}