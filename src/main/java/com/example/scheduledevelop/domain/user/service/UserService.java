package com.example.scheduledevelop.domain.user.service;

import com.example.scheduledevelop.domain.schedule.entity.Schedule;
import com.example.scheduledevelop.domain.schedule.repository.ScheduleRepository;
import com.example.scheduledevelop.domain.user.dto.*;
import com.example.scheduledevelop.domain.user.entity.User;
import com.example.scheduledevelop.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User login(LoginRequestDto requestDto, HttpServletRequest request) {
        // 이메일, 비밀번호로 사용자 찾기
        User user = userRepository.findByEmail(requestDto.getEmail())
                .filter(user1 -> requestDto.getPassword().equals(user1.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 잘못되었습니다."));

        // 세션 가져오기 (없으면 생성)
        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setMaxInactiveInterval(1800); // 30분 유지
        return user;
    }

    @Transactional
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("로그아웃 성공");
    }


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
    public UserResponseDto updateUser(Long id, HttpSession session, UserUpdateRequestDto dto){
        // 세션에서 userId 가져오기
        Long sessionUserId = (Long) session.getAttribute("userId");

        // 세션의 userId와 요청된 id가 일치하는지 확인
        if (!sessionUserId.equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신의 정보만 변경할 수 있습니다.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));
        //이름, 이메일 값이 존재할 경우에만 업데이트
        if(dto.getUsername() != null && !dto.getUsername().trim().isEmpty()){
            user.updateUsername(dto.getUsername());
        }

        if(dto.getEmail() != null && !dto.getEmail().trim().isEmpty()){
            user.updateEmail(dto.getEmail());
        }

        return new UserResponseDto(user);
    }

    @Transactional
    public void updatePassword(Long id, HttpSession session, String oldPassword, String newPassword) {
        // 세션에서 userId 가져오기
        Long sessionUserId = (Long) session.getAttribute("userId");

        // 세션의 userId와 요청된 id가 일치하는지 확인
        if (!sessionUserId.equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신의 비밀번호만 변경할 수 있습니다.");
        }

        User findUser = userRepository.findUserById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."));

        if(!findUser.getPassword().equals(oldPassword)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }
        findUser.updatePassword(newPassword);
    }

    public void deleteUser(Long id, HttpSession session, String password) {
        // 세션에서 userId 가져오기
        Long sessionUserId = (Long) session.getAttribute("userId");

        // 세션의 userId와 요청된 id가 일치하는지 확인
        if (!sessionUserId.equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신의 계정만 삭제할 수 있습니다.");
        }

        // 유저 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        // 비밀번호 검증
        if (!user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        // 유저 삭제
        userRepository.delete(user);

        // 세션 무효화 (자동 로그아웃)
        session.invalidate();
    }
}
