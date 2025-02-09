package com.example.scheduledevelop.domain.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleCreateRequestDto {
    private String writer;
    private String title;
    private String contents;
}
