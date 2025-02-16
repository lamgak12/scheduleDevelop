package com.example.scheduledevelop.domain.user.controller;

import com.example.scheduledevelop.domain.user.dto.request.UserDeleteRequestDto;
import com.example.scheduledevelop.domain.user.dto.request.UserPasswordUpdateRequestDto;
import com.example.scheduledevelop.domain.user.dto.request.UserUpdateRequestDto;
import com.example.scheduledevelop.domain.user.dto.response.UserResponseDto;
import com.example.scheduledevelop.domain.user.service.UserService;
import com.example.scheduledevelop.global.PageResponseDto;
import com.example.scheduledevelop.global.filter.Const;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<PageResponseDto<UserResponseDto>> findAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        PageResponseDto<UserResponseDto> users = userService.findAllUsers(pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(
            @PathVariable Long id
    ) {
        UserResponseDto responseDto = userService.findUserById(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDto requestDto,
            @SessionAttribute(Const.LOGIN_USER) Long sessionUserId
    ){
        UserResponseDto responseDto = userService.updateUser(id, requestDto, sessionUserId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<String>updateUserPassword(
            @PathVariable Long id,
            @Valid @RequestBody UserPasswordUpdateRequestDto requestDto,
            @SessionAttribute(Const.LOGIN_USER) Long sessionUserId,
            HttpServletRequest request
    ){
        userService.updatePassword(id, requestDto.getOldPassword(), requestDto.getNewPassword(),sessionUserId);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return new ResponseEntity<>("수정을 성공하였습니다.",HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String>deleteUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDeleteRequestDto requestDto,
            @SessionAttribute(Const.LOGIN_USER) Long sessionUserId,
            HttpServletRequest request
    ){
        userService.deleteUser(id, requestDto.getPassword(), sessionUserId);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return new ResponseEntity<>("삭제를 성공하였습니다.",HttpStatus.NO_CONTENT);
    }
}
