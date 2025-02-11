package com.example.scheduledevelop.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateRequestDto {
    private String username;
    private String email;
    private String password;
}
