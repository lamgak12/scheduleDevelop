package com.example.scheduledevelop.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPasswordUpdateRequestDto {
    private String email;
    private String oldPassword;
    private String newPassword;
}
