package com.example.scheduledevelop.global.exception.schedule;

import com.example.scheduledevelop.global.exception.CustomException;
import com.example.scheduledevelop.global.exception.ErrorCode;

public class ScheduleNotFoundException extends CustomException {
    public ScheduleNotFoundException() {
        super(ErrorCode.SCHEDULE_NOT_FOUND);
    }
}
