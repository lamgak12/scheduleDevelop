package com.example.scheduledevelop.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserDeleteRequestDto {
    @NotBlank(message = "비밀번호는 필수 입력값 입니다.")
    private String password;

}
