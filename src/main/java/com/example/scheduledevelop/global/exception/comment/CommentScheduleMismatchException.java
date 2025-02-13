package com.example.scheduledevelop.global.exception.comment;

import com.example.scheduledevelop.global.exception.CustomException;
import com.example.scheduledevelop.global.exception.ErrorCode;

public class CommentScheduleMismatchException extends CustomException {
    public CommentScheduleMismatchException() {
        super(ErrorCode.COMMENT_SCHEDULE_MISMATCH);
    }
}
