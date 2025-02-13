package com.example.scheduledevelop.global.exception.comment;

import com.example.scheduledevelop.global.exception.CustomException;
import com.example.scheduledevelop.global.exception.ErrorCode;

public class UnauthorizedCommentAccessException extends CustomException {
    public UnauthorizedCommentAccessException() {
        super(ErrorCode.UNAUTHORIZED_COMMENT_ACCESS);
    }
}
