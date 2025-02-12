package com.example.scheduledevelop.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreateRequestDto {
   @NotBlank(message = "이름은 필수 입력값입니다.")
   private String username;
   @Email(message = "올바른 이메일 형식을 입력하세요.")
   @NotBlank(message = "이메일은 필수 입력값입니다.")
   private String email;
   @NotBlank(message = "비밀번호는 필수 입력값입니다.")
   private String password;
}
