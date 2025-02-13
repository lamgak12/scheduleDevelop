package com.example.scheduledevelop.global.exception.user;

import com.example.scheduledevelop.global.exception.CustomException;
import com.example.scheduledevelop.global.exception.ErrorCode;

public class IncorrectPasswordException extends CustomException {
    public IncorrectPasswordException(ErrorCode errorCode) {
        super(ErrorCode.INCORRECT_PASSWORD);
    }
}
