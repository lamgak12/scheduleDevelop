package com.example.scheduledevelop.domain.user.controller;

import com.example.scheduledevelop.domain.user.dto.UserCreateRequestDto;
import com.example.scheduledevelop.domain.user.dto.UserResponseDto;
import com.example.scheduledevelop.domain.user.dto.UserUpdateRequestDto;
import com.example.scheduledevelop.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @RequestBody UserCreateRequestDto requestDto
    ){
     UserResponseDto responseDto  = userService.createUser(requestDto);
      return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAllUsers(){
       List<UserResponseDto> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.CREATED);
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
            @RequestBody UserUpdateRequestDto requestDto
            ){
        UserResponseDto responseDto = userService.updateUser(id, requestDto);
      return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
