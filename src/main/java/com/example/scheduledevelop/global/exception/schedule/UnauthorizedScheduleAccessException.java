package com.example.scheduledevelop.global.exception.schedule;

import com.example.scheduledevelop.global.exception.CustomException;
import com.example.scheduledevelop.global.exception.ErrorCode;

public class UnauthorizedScheduleAccessException extends CustomException{
    public UnauthorizedScheduleAccessException() {
        super(ErrorCode.UNAUTHORIZED_SCHEDULE_ACCESS);
    }
}
