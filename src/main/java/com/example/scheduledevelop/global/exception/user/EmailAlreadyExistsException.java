package com.example.scheduledevelop.global.exception.user;

import com.example.scheduledevelop.global.exception.CustomException;
import com.example.scheduledevelop.global.exception.ErrorCode;

public class EmailAlreadyExistsException extends CustomException {
    public EmailAlreadyExistsException() {
        super(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
}
