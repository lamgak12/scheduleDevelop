package com.example.scheduledevelop.domain.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleCreateRequestDto {
    @Size(min = 5, max = 30, message = "제목은 5~30자로 입력해야 합니다.")
    @NotBlank(message = "제목은 필수 입력값입니다.")
    private String title;
    @Size(min = 10, max = 200, message = "내용은 10~200자로 입력해야 합니다.")
    @NotBlank(message = "내용은 필수 입력값입니다.")
    private String contents;
}
