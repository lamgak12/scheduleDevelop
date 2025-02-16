package com.example.scheduledevelop.domain.auth.controller;

import com.example.scheduledevelop.domain.auth.dto.request.LoginRequestDto;
import com.example.scheduledevelop.domain.auth.dto.request.UserSignupRequestDto;
import com.example.scheduledevelop.domain.user.dto.response.UserResponseDto;
import com.example.scheduledevelop.domain.user.entity.User;
import com.example.scheduledevelop.domain.user.service.UserService;
import com.example.scheduledevelop.global.exception.CustomException;
import com.example.scheduledevelop.global.exception.ErrorCode;
import com.example.scheduledevelop.global.filter.Const;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
public class AuthController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestBody UserSignupRequestDto requestDto
    ){
        UserResponseDto responseDto  = userService.signupUser(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletRequest request){

        // 이미 로그인한 사용자인지 확인
        HttpSession existingSession = request.getSession(false);
        if (existingSession != null && existingSession.getAttribute(Const.LOGIN_USER) != null) {
            throw new CustomException(ErrorCode.ALREADY_LOGGED_IN);
        }

        User user = userService.login(requestDto);

        // 세션 생성
        HttpSession session = request.getSession(true);
        session.setAttribute(Const.LOGIN_USER,user.getId());
        session.setMaxInactiveInterval(1800); // 30분 유지

        return new ResponseEntity<>("로그인에 성공하였습니다.",HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return new ResponseEntity<>("로그아웃에 성공하였습니다.",HttpStatus.OK);
    }
}
