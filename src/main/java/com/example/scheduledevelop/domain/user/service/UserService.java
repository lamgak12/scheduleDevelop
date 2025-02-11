package com.example.scheduledevelop.domain.user.service;

import com.example.scheduledevelop.domain.user.dto.UserCreateRequestDto;
import com.example.scheduledevelop.domain.user.dto.UserPasswordUpdateRequestDto;
import com.example.scheduledevelop.domain.user.dto.UserResponseDto;
import com.example.scheduledevelop.domain.user.dto.UserUpdateRequestDto;
import com.example.scheduledevelop.domain.user.entity.User;
import com.example.scheduledevelop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAllUsers() {
       return userRepository.findAll()
               .stream()
               .map(UserResponseDto::new)
               .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDto findUserById(Long id) {
        User findUser = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));
        return new UserResponseDto(findUser);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto dto){
        User user = userRepository.findById(id)
                .filter(user1 -> dto.getPassword().equals(user1.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));
        String newUsername = user.getUsername();
        String newEmail = user.getEmail();
        if(dto.getUsername() != null && !dto.getUsername().trim().isEmpty()){
            newUsername = dto.getUsername();
        }
        if(dto.getEmail() != null && !dto.getEmail().trim().isEmpty()){
            newEmail = dto.getEmail();
        }
        user.update(newUsername, newEmail);
        return new UserResponseDto(user);
    }

    @Transactional
    public void updatePassword(Long id, String email, String oldPassword, String newPassword) {
        User findUser = userRepository.findUserById(id)
                .filter(user -> user.getEmail().equals(email))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));

        if(!findUser.getPassword().equals(oldPassword)){
            System.out.println("이게 null이라고?" + oldPassword);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }
        findUser.updatePassword(newPassword);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));
        userRepository.delete(user);
    }
}
