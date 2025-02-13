package com.example.scheduledevelop.global.exception.user;

import com.example.scheduledevelop.global.exception.CustomException;
import com.example.scheduledevelop.global.exception.ErrorCode;

public class UnauthorizedAccessException extends CustomException {
    public UnauthorizedAccessException() {
        super(ErrorCode.UNAUTHORIZED_ACCESS);
    }
}
