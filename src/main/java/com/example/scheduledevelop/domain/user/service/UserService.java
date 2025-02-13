package com.example.scheduledevelop.domain.user.service;

import com.example.scheduledevelop.domain.user.dto.request.LoginRequestDto;
import com.example.scheduledevelop.domain.user.dto.request.UserCreateRequestDto;
import com.example.scheduledevelop.domain.user.dto.request.UserUpdateRequestDto;
import com.example.scheduledevelop.domain.user.dto.response.UserResponseDto;
import com.example.scheduledevelop.domain.user.entity.User;
import com.example.scheduledevelop.domain.user.repository.UserRepository;
import com.example.scheduledevelop.global.PageResponseDto;
import com.example.scheduledevelop.global.config.PasswordEncoder;
import com.example.scheduledevelop.global.exception.CustomException;
import com.example.scheduledevelop.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User login(LoginRequestDto requestDto, HttpServletRequest request) {
        // 이메일, 비밀번호로 사용자 찾기
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

        HttpSession existingSession = request.getSession(false);
        if (existingSession != null && existingSession.getAttribute("userId") != null) {
            throw new CustomException(ErrorCode.ALREADY_LOGGED_IN);
        }

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

    @Transactional
    public UserResponseDto createUser(UserCreateRequestDto dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(user -> {
                    throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
                });
        User user = new User(dto.getUsername(), dto.getEmail(), encodedPassword);
        User savedUser = userRepository.save(user);

        return new UserResponseDto(savedUser);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<UserResponseDto> findAllUsers(Pageable pageable) {
        Page<UserResponseDto> userPage = userRepository.findAll(pageable).map(UserResponseDto::new);
        return new PageResponseDto<>(userPage);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findUserById(Long id) {
        User findUser = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return new UserResponseDto(findUser);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, HttpSession session, UserUpdateRequestDto dto){
        // 세션에서 userId 가져오기
        Long sessionUserId = (Long) session.getAttribute("userId");

        // 세션의 userId와 요청된 id가 일치하는지 확인
        if (!sessionUserId.equals(id)) {
            //자신의 정보만 변경할 수 있습니다.
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

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
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        User findUser = userRepository.findUserById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(oldPassword, findUser.getPassword())) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }
        String encodedPassword = passwordEncoder.encode(newPassword);
        findUser.updatePassword(encodedPassword);

        session.invalidate();
    }

    public void deleteUser(Long id, String password,  HttpSession session) {
        // 세션에서 userId 가져오기
        Long sessionUserId = (Long) session.getAttribute("userId");

        // 세션의 userId와 요청된 id가 일치하는지 확인
        if (!sessionUserId.equals(id)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        // 유저 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        // 비밀번호 검증
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

        // 유저 삭제
        userRepository.delete(user);

        // 세션 무효화 (자동 로그아웃)
        session.invalidate();
    }
}
