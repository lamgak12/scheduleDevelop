package com.example.scheduledevelop.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPasswordUpdateRequestDto {
    @NotBlank(message = "현재 비밀번호는 필수 입력값 입니다.")
    private String oldPassword;
    @NotBlank(message = "새로운 비밀번호는 필수 입력값 입니다.")
    private String newPassword;
}
