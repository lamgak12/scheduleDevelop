package com.example.scheduledevelop.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 유저 관련 오류
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    INCORRECT_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    ALREADY_LOGGED_IN(HttpStatus.CONFLICT, "이미 로그인 중인 사용자입니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // 일정 관련 오류
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 일정을 찾을 수 없습니다."),
    UNAUTHORIZED_SCHEDULE_ACCESS(HttpStatus.FORBIDDEN, "자신이 작성한 일정만 수정 또는 삭제할 수 있습니다."),

    // 댓글 관련 오류
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),
    UNAUTHORIZED_COMMENT_ACCESS(HttpStatus.FORBIDDEN, "자신이 작성한 댓글만 수정 또는 삭제할 수 있습니다."),
    COMMENT_SCHEDULE_MISMATCH(HttpStatus.BAD_REQUEST, "해당 일정에 속하지 않는 댓글입니다."),

    // 공통 오류
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
