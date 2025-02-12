package com.example.scheduledevelop.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateRequestDto {
    private String username;
    @Email(message = "올바른 이메일 형식을 입력하세요.")
    private String email;
}
