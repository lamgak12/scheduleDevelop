package com.example.scheduledevelop.domain.user.controller;

import com.example.scheduledevelop.domain.user.dto.*;
import com.example.scheduledevelop.domain.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
           @Valid @RequestBody UserCreateRequestDto requestDto
    ){
     UserResponseDto responseDto  = userService.createUser(requestDto);
      return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAllUsers(){
       List<UserResponseDto> users = userService.findAllUsers();
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
            HttpSession session
            ){
        UserResponseDto responseDto = userService.updateUser(id, session, requestDto);
      return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void>updateUserPassword(
        @PathVariable Long id,
      @Valid @RequestBody UserPasswordUpdateRequestDto requestDto,
        HttpSession session
    ){
        userService.updatePassword(id, session, requestDto.getOldPassword(), requestDto.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deleteUser(
            @PathVariable Long id,
          @Valid @RequestBody UserDeleteRequestDto requestDto,
            HttpSession session

    ){
        userService.deleteUser(id, session, requestDto.getPassword());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
