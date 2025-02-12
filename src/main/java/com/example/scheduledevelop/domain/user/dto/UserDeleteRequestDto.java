package com.example.scheduledevelop.domain.user.dto;

import lombok.Getter;

@Getter
public class UserDeleteRequestDto {
    private String password;

    public UserDeleteRequestDto(String password) {
        this.password = password;
    }
}
