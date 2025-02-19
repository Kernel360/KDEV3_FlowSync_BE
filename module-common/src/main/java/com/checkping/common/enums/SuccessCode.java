package com.checkping.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    OK(HttpStatus.OK, "요청이 성공했습니다."),

    // NOTICE
    NOTICE_REGISTER(HttpStatus.OK, "공지사항 등록이 완료되었습니다."),
    NOTICE_UPDATE(HttpStatus.OK, "공지사항 수정이 완료되었습니다."),
    NOTICE_DELETE(HttpStatus.OK, "공지사항 삭제가 완료되었습니다."),

    // ORGANIZATION
    ORGANIZATION_REGISTER(HttpStatus.OK, "업체 등록이 완료되었습니다."),
    ORGANIZATION_UPDATE(HttpStatus.OK, "업체 수정이 완료되었습니다."),
    ORGANIZATION_DELETE(HttpStatus.OK, "업체 삭제가 완료되었습니다."),
    ORGANIZATION_CHANGE_STATUS(HttpStatus.OK, "업체 상태 변경이 완료되었습니다."),

    // MEMBER
    MEMBER_REGISTER(HttpStatus.OK, "회원 등록이 완료되었습니다."),
    MEMBER_UPDATE(HttpStatus.OK, "회원 수정이 완료되었습니다."),
    MEMBER_DELETE(HttpStatus.OK, "회원 삭제가 완료되었습니다."),
    MEMBER_SIGNATURE_UPLOAD(HttpStatus.OK, "서명 파일 업로드가 완료되었습니다."),
    MEMBER_CHANGE_PASSWORD(HttpStatus.OK, "비밀번호 변경이 완료되었습니다."),
    MEMBER_ACTIVATE(HttpStatus.OK, "회원 활성화가 완료되었습니다."),
    MEMBER_DEACTIVATE(HttpStatus.OK, "회원 비활성화가 완료되었습니다."),

    // PROJECT
    PROJECT_REGISTER(HttpStatus.OK, "프로젝트 등록이 완료되었습니다."),
    PROJECT_UPDATE(HttpStatus.OK, "프로젝트 수정이 완료되었습니다."),
    PROJECT_DELETE(HttpStatus.OK, "프로젝트 삭제가 완료되었습니다."),
    PROJECT_MANAGEMENT_STEP_UPDATE(HttpStatus.OK, "프로젝트 관리 단계 수정이 완료되었습니다."),
    PROJECT_PROGRESS_STEP_REGISTER(HttpStatus.OK, "프로젝트 진행 단계 등록이 완료되었습니다."),
    PROJECT_PROGRESS_STEP_UPDATE(HttpStatus.OK, "프로젝트 진행 단계 수정이 완료되었습니다."),
    PROJECT_PROGRESS_STEP_DELETE(HttpStatus.OK, "프로젝트 진행 단계 삭제가 완료되었습니다."),

    // APPROVAL
    APPROVAL_REGISTER(HttpStatus.OK, "결재 등록이 완료되었습니다."),
    APPROVAL_UPDATE(HttpStatus.OK, "결재 수정이 완료되었습니다."),
    APPROVAL_DELETE(HttpStatus.OK, "결재 삭제가 완료되었습니다."),
    APPROVAL_CONFIRM(HttpStatus.OK, "결재 승인이 완료되었습니다."),
    APPROVAL_REJECT(HttpStatus.OK, "결재 반려가 완료되었습니다."),
    APPROVAL_COMMENT_REGISTER(HttpStatus.OK, "결재 댓글 등록이 완료되었습니다."),
    APPROVAL_COMMENT_UPDATE(HttpStatus.OK, "결재 댓글 수정이 완료되었습니다."),
    APPROVAL_COMMENT_DELETE(HttpStatus.OK, "결재 댓글 삭제가 완료되었습니다."),

    // QUESTION
    QUESTION_REGISTER(HttpStatus.OK, "질문 등록이 완료되었습니다."),
    QUESTION_UPDATE(HttpStatus.OK, "질문 수정이 완료되었습니다."),
    QUESTION_DELETE(HttpStatus.OK, "질문 삭제가 완료되었습니다."),
    QUESTION_ANSWER_REGISTER(HttpStatus.OK, "질문 답변 등록이 완료되었습니다."),
    QUESTION_ANSWER_UPDATE(HttpStatus.OK, "질문 답변 수정이 완료되었습니다."),
    QUESTION_COMMENT_REGISTER(HttpStatus.OK, "질문 댓글 등록이 완료되었습니다."),
    QUESTION_COMMENT_UPDATE(HttpStatus.OK, "질문 댓글 수정이 완료되었습니다."),
    QUESTION_COMMENT_DELETE(HttpStatus.OK, "질문 댓글 삭제가 완료되었습니다."),

    // FILE
    FILE_UPLOAD(HttpStatus.OK, "파일 업로드가 완료되었습니다."),
    FILE_PRIVATE_UPLOAD(HttpStatus.OK, "보안 파일 업로드가 완료되었습니다.");

    private final HttpStatus status;
    private final String message;

    public int getStatusCode() {
        return status.value();
    }
}
