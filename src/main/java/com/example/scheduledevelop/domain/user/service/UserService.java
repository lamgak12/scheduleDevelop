package com.example.scheduledevelop.domain.user.service;

import com.example.scheduledevelop.domain.user.dto.UserCreateRequestDto;
import com.example.scheduledevelop.domain.user.dto.UserResponseDto;
import com.example.scheduledevelop.domain.user.entity.User;
import com.example.scheduledevelop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
   private final UserRepository userRepository;

    public UserResponseDto createUser(UserCreateRequestDto dto) {
        User user = new User(dto.getUsername(), dto.getEmail(), dto.getPassword());
        User savedUser = userRepository.save(user);
        return new UserResponseDto(savedUser);
    }

    public List<UserResponseDto> findAllUsers() {
       return userRepository.findAll()
               .stream()
               .map(UserResponseDto::new)
               .collect(Collectors.toList());
    }

    public UserResponseDto findUserById(Long id) {
        User findUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));
        return new UserResponseDto(findUser);
    }
}
