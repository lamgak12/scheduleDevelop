package com.example.scheduledevelop.domain.user.controller;

import com.example.scheduledevelop.domain.user.dto.LoginRequestDto;
import com.example.scheduledevelop.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class LoginController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequestDto requestDto, HttpServletRequest request){
            userService.login(requestDto, request);
            return new ResponseEntity<>(HttpStatus.OK); // 로그인 성공 시 OK 응답 (데이터 없음)
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        return userService.logout(request);
    }
}
