package com.example.scheduledevelop.domain.user.controller;

import com.example.scheduledevelop.domain.user.dto.request.UserCreateRequestDto;
import com.example.scheduledevelop.domain.user.dto.request.UserDeleteRequestDto;
import com.example.scheduledevelop.domain.user.dto.request.UserPasswordUpdateRequestDto;
import com.example.scheduledevelop.domain.user.dto.request.UserUpdateRequestDto;
import com.example.scheduledevelop.domain.user.dto.response.UserResponseDto;
import com.example.scheduledevelop.domain.user.service.UserService;
import com.example.scheduledevelop.global.PageResponseDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



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
    public ResponseEntity<PageResponseDto<UserResponseDto>> findAllUsers(
            @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable
    ){
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
        userService.deleteUser(id, requestDto.getPassword(), session);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
