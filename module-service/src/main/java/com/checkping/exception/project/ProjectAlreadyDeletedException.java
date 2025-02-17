package com.checkping.exception.project;

import com.checkping.common.enums.ErrorCode;

public class ProjectAlreadyDeletedException extends ProjectException {

    public ProjectAlreadyDeletedException() {
        super("이미 삭제된 프로젝트입니다.", ErrorCode.BAD_REQUEST);
    }
}
