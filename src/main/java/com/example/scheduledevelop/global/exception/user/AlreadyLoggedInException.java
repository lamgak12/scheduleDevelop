package com.example.scheduledevelop.global.exception.user;

import com.example.scheduledevelop.global.exception.CustomException;
import com.example.scheduledevelop.global.exception.ErrorCode;

public class AlreadyLoggedInException extends CustomException {
    public AlreadyLoggedInException() {
        super(ErrorCode.ALREADY_LOGGED_IN);
    }
}
