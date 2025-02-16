package com.example.scheduledevelop.global.exception.schedule;

import com.example.scheduledevelop.global.exception.CustomException;
import com.example.scheduledevelop.global.exception.ErrorCode;

public class InvalidUpdateRequestException extends CustomException {
    public InvalidUpdateRequestException(ErrorCode errorCode) {
        super(ErrorCode.INVALID_UPDATE_REQUEST);
    }
}
